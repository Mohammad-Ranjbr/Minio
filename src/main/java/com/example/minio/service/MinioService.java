package com.example.minio.service;

import java.util.List;

public interface MinioService {

    boolean bucketExists(String bucketName);
    String makeBucket(String bucketName);
    String makeBucketWithObjectLock(String bucketName);
    String uploadObjectToBucket(String bucketName, String objectName);
    String uploadObjectToBucketWithS3DefaultEncryption(String bucketName, String objectName);
    String setBucketEncryption(String bucketName);
    String getObjectFromBucket(String bucketName, String objectName);
    String listBuckets();
    List<String> listObjects(String bucketName);
    String setBucketVersioning(String bucketName);
    String getBucketVersioning(String bucketName);
    String getObjectVersions(String bucketName, String objectName);
    String getObjectByVersion(String bucketName, String objectName, String versionId);

}
