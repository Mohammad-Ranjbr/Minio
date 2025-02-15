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
    String getBucketLifecycle(String bucketName);
    String setBucketTags(String bucketNam);
    String getBucketTags(String bucketName);
    String deleteBucketTags(String bucketName);
    String setObjectTags(String bucketName, String objectName);
    String getObjectTags(String bucketName, String objectName);
    String deleteObjectTags(String bucketName, String objectName);
    String removeBucket(String bucketName);
    String removeObject(String bucketName, String objectName);
    String removeObjects(String bucketName);
    String copyObject(String sourceBucketName, String sourceObjectName, String bucketName, String objectName);

}
