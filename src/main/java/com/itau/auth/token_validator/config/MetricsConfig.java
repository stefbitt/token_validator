package com.itau.auth.token_validator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

@Configuration
@Profile("aws")
public class MetricsConfig {

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {

        RetryPolicy retryPolicy = RetryPolicy.builder()
                .numRetries(3)
                .build();

        ClientOverrideConfiguration overrideConfiguration = ClientOverrideConfiguration.builder()
                .retryPolicy(retryPolicy)
                .build();

        return CloudWatchAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .overrideConfiguration(overrideConfiguration)
                .region(Region.US_EAST_1)
                .build();
    }
}
