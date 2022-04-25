package com.nikolai.kuskusimages.config;

import com.nikolai.kuskusimages.service.ImageService;
import com.nikolai.kuskusimages.service.impl.MainImageService;
import com.nikolai.kuskusimages.service.impl.StepImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class BaseConfig {

    @Value("${s3.bucket.main}")
    String mainImagesBucketName;

    @Value("${s3.bucket.step}")
    String stepImagesBucketName;

    @Value("${s3.image.empty}")
    String emptyImageName;

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
    ImageService mainImageService(S3AsyncClient s3AsyncClient) {
        return new MainImageService(s3AsyncClient, mainImagesBucketName, emptyImageName);
    }

    @Bean
    ImageService stepImageService(S3AsyncClient s3AsyncClient) {
        return new StepImageService(s3AsyncClient, stepImagesBucketName, emptyImageName);
    }
}
