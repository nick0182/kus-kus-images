package com.nikolai.kuskusimages.service.impl;

import com.nikolai.kuskusimages.service.ImageService;
import com.nikolai.kuskusimages.service.Type;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
public class MainImageService extends ImageService {

    public MainImageService(S3AsyncClient s3AsyncClient, String bucketName, String emptyImageName) {
        super(s3AsyncClient, bucketName, emptyImageName);
    }

    @Override
    public Type type() {
        return Type.MAIN;
    }

    @Override
    protected String resolveKey(String imageId) {
        return imageId + "/" + imageId + ".jpeg";
    }
}
