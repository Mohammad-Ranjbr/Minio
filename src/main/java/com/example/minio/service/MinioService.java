package com.example.minio.service;

public interface MinioService {

    boolean bucketExists(String bucketName);
    String makeBucket(String bucketName);

}
