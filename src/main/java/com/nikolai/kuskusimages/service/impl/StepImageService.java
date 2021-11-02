package com.nikolai.kuskusimages.service.impl;

import com.nikolai.kuskusimages.service.ImageService;
import com.nikolai.kuskusimages.service.Type;
import software.amazon.awssdk.services.s3.S3AsyncClient;

public class StepImageService extends ImageService {

    public StepImageService(S3AsyncClient s3AsyncClient, String bucketName, String emptyImageName) {
        super(s3AsyncClient, bucketName, emptyImageName);
    }

    @Override
    public Type type() {
        return Type.STEP;
    }

    @Override
    protected String resolveKey(String imageId) {
        String folder = imageId.split("\\.")[0];
        return folder + "/" + imageId + ".jpeg";
    }
}
