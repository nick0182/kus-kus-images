package com.nikolai.kuskusimages.service;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.util.concurrent.CompletableFuture;

public abstract class ImageService {

    private final S3AsyncClient s3AsyncClient;

    private final String bucketName;

    private final String emptyImageName;

    protected ImageService(S3AsyncClient s3AsyncClient, String bucketName, String emptyImageName) {
        this.s3AsyncClient = s3AsyncClient;
        this.bucketName = bucketName;
        this.emptyImageName = emptyImageName;
    }

    public CompletableFuture<byte[]> fetchImage(String imageId) {
        return fetchImageByKey(resolveKey(imageId)).exceptionallyCompose(ex -> fetchImageByKey(emptyImageName));
    }

    private CompletableFuture<byte[]> fetchImageByKey(String key) {
        return s3AsyncClient
                .getObject(GetObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(key).build(), AsyncResponseTransformer.toBytes())
                .thenApply(ResponseBytes::asByteArray);
    }

    public abstract Type type();

    protected abstract String resolveKey(String imageId);
}
