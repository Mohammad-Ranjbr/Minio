package com.example.minio.service.impl;

import com.example.minio.service.MinioService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;
    private final static Logger logger = LoggerFactory.getLogger(MinioService.class);

    @Autowired
    public MinioServiceImpl(MinioClient minioClient){
        this.minioClient = minioClient;
    }

    @Override
    public boolean bucketExists(String bucketName) {
        boolean exists = false;
        try{
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (found){
                logger.info(bucketName + " exists");
                exists = true;
            } else {
                logger.info(bucketName + " does not exists");
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception){
            logger.error("Error occurred: " + exception);
        }
        return exists;
    }

    @Override
    public String makeBucket(String bucketName) {
        String message = "";
        if(!bucketExists(bucketName)){
            try{
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                message = bucketName + "is created successfully";
                logger.info(message);
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + "already is exists";
        }
        return message;
    }

    @Override
    public String makeBucketWithObjectLock(String bucketName) {
        // When the Object Lock feature is enabled, you can specify that objects in the bucket are protected and no one can modify or delete them, even if they have system administration.
        // Two main Object Lock modes:
        // Governance Mode:
        // In this mode, users with special permissions (usually administrators or those with high permissions) can delete or modify objects, but it will be impossible for users to change or delete objects.
        // This mode allows you to protect objects, but at the same time there may be changes or emergencies for legal with special access.
        // Compliance Mode:
        // In this mode, Object Lock is fully and irreversibly enabled. No one can delete or modify locked objects, even if they have administrative access. This mode is usually suitable for use in industries that require compliance with regulations (such as legal record keeping and accounting).
        // If Object Lock is enabled in Compliance Mode, this lock cannot be changed from its application.
        // Governance Mode is enabled by default, unless you specifically configure Compliance Mode.

        String message = "";
        if(!bucketExists(bucketName)){
            try{
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .objectLock(true)
                                .build());
                message = bucketName + "is created successfully";
                logger.info(message);
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + "already is exists";
        }
        return message;
    }

}
