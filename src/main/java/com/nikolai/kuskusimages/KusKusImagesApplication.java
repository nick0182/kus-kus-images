package com.nikolai.kuskusimages;

import com.nikolai.kuskusimages.service.ImageService;
import com.nikolai.kuskusimages.service.impl.MainImageService;
import com.nikolai.kuskusimages.service.impl.StepImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.profiles.ProfileFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.nio.file.Paths;

import static software.amazon.awssdk.utils.UserHomeDirectoryUtils.userHomeDirectory;

@SpringBootApplication
public class KusKusImagesApplication {

    @Value("${s3.bucket.main}")
    String mainImagesBucketName;

    @Value("${s3.bucket.step}")
    String stepImagesBucketName;

    @Value("${s3.image.empty}")
    String emptyImageName;

    public static void main(String[] args) {
        SpringApplication.run(KusKusImagesApplication.class, args);
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }

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

    @Bean
    ImageService mainImageService(S3AsyncClient s3AsyncClient) {
        return new MainImageService(s3AsyncClient, mainImagesBucketName, emptyImageName);
    }

    @Bean
    ImageService stepImageService(S3AsyncClient s3AsyncClient) {
        return new StepImageService(s3AsyncClient, stepImagesBucketName, emptyImageName);
    }
}
