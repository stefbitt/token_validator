package com.itau.auth.token_validator.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

@Configuration
@Profile("aws")
public class MetricsConfig {

    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry() {
        CloudWatchConfig cloudWatchConfig = new CloudWatchConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String namespace() {
                return "token-validator/Metrics";
            }
        };

        CloudWatchAsyncClient cloudWatchAsyncClient = CloudWatchAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();

        return new CloudWatchMeterRegistry(cloudWatchConfig, Clock.SYSTEM, cloudWatchAsyncClient);
    }
}
