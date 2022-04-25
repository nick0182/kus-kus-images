package com.nikolai.kuskusimages.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.profiles.ProfileFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.nio.file.Paths;

import static software.amazon.awssdk.utils.UserHomeDirectoryUtils.userHomeDirectory;

@Configuration
@Profile("local")
public class LocalConfig {

    @Bean
    S3AsyncClient s3AsyncClient() {
        return S3AsyncClient
                .builder()
                .credentialsProvider(ProfileCredentialsProvider.builder().profileFile(ProfileFile
                        .aggregator()
                        .applyMutation(aggregator -> aggregator.addFile(ProfileFile
                                .builder()
                                .content(Paths.get(userHomeDirectory(), ".aws", "nikolai", "credentials"))
                                .type(ProfileFile.Type.CREDENTIALS)
                                .build()))
                        .applyMutation(aggregator -> aggregator.addFile(ProfileFile
                                .builder()
                                .content(Paths.get(userHomeDirectory(), ".aws", "nikolai", "config"))
                                .type(ProfileFile.Type.CONFIGURATION)
                                .build()))
                        .build()).profileName("default").build())
                .region(Region.ME_SOUTH_1)
                .build();
    }
}
