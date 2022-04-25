package com.nikolai.kuskusimages.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
@Profile("prod")
public class CloudConfig {

    @Bean
    S3AsyncClient s3AsyncClient() {
        WebIdentityTokenFileCredentialsProvider webIdentityTokenFileCredentialsProvider =
                WebIdentityTokenFileCredentialsProvider.builder().build();
        webIdentityTokenFileCredentialsProvider.resolveCredentials(); // exit with exception if token file env not set
        return S3AsyncClient
                .builder()
                .credentialsProvider(webIdentityTokenFileCredentialsProvider)
                .region(Region.ME_SOUTH_1)
                .build();
    }
}
