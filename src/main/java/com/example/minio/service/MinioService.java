package com.example.minio.service;

import java.util.List;

public interface MinioService {

    boolean bucketExists(String bucketName);
    String makeBucket(String bucketName);
    String makeBucketWithObjectLock(String bucketName);
    String uploadStringToBucket(String bucketName, String objectName);
    String uploadStringToBucketWithS3DefaultEncryption(String bucketName, String objectName);
    String setBucketEncryption(String bucketName);
    String getStringFromBucket(String bucketName, String objectName);
    String listBuckets();

}
