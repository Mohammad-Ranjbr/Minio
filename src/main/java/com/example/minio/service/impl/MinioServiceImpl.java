package com.example.minio.service.impl;

import com.example.minio.service.MinioService;
import io.minio.BucketExistsArgs;
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
        try{
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (found){
                logger.info(bucketName + " exists");
                return true;
            } else {
                logger.info(bucketName + " does not exists");
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception){
            logger.error("Error occurred: " + exception);
        }
        return false;
    }

}
