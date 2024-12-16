package com.example.minio.controller;

import com.example.minio.service.MinioService;
import io.minio.credentials.AssumeRoleBaseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/minio")
public class MinioController {

    private final MinioService minioService;
    private final static Logger logger = LoggerFactory.getLogger(MinioController.class);

    @Autowired
    public MinioController(MinioService minioService){
        this.minioService = minioService;
    }

    @GetMapping("/exists-bucket/{bucketName}")
    public ResponseEntity<String> existsBucket(@PathVariable("bucketName") String bucketName){
        boolean found = minioService.bucketExists(bucketName);
        return new ResponseEntity<>("Bucket exists: " + found, HttpStatus.OK);
    }

}
