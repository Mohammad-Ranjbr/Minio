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

}
