package com.itau.auth.token_validator.metrics;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

import java.time.Instant;

@Aspect
@Component
@Profile("aws")
@Log4j2
public class MetricAspect {

    @Autowired
    private CloudWatchAsyncClient cloudWatchAsyncClient;

    @Value("${management.metrics.export.cloudwatch.namespace}")
    private String namespace;

    @Value("${spring.application.name}")
    private String appName;

    @Around("@annotation(metric)")
    public Object sendMetric(ProceedingJoinPoint joinPoint, Metric metric) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("Method execution time in {} Millis", duration);

            Dimension serviceDimension = Dimension.builder()
                    .name("ServiceName")
                    .value(appName)
                    .build();

            Dimension methodDimension = Dimension.builder()
                    .name("Method")
                    .value(joinPoint.getSignature().getName())
                    .build();

            MetricDatum datum = MetricDatum.builder()
                    .metricName(metric.name())
                    .unit(StandardUnit.fromValue(metric.unit()))
                    .value((double) duration)
                    .timestamp(Instant.now())
                    .dimensions(serviceDimension, methodDimension)
                    .build();

            PutMetricDataRequest request = PutMetricDataRequest.builder()
                    .namespace(namespace)
                    .metricData(datum)
                    .build();

            cloudWatchAsyncClient.putMetricData(request).handle((response, exception) -> {
                if (exception != null) {
                    log.error("Failed to send metric to CloudWatch for {}: {}", joinPoint.getSignature()
                            .getName(), exception.getMessage(), exception);
                } else {
                    log.debug("Successfully sent metric to CloudWatch for {}", joinPoint.getSignature().getName());
                }
                return null;
            });
        }

        return result;
    }
}

