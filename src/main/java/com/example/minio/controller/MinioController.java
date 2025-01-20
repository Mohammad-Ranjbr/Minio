package com.example.minio.controller;

import com.example.minio.service.MinioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/minio")
public class MinioController {

    private final MinioService minioService;
    private final static Logger logger = LoggerFactory.getLogger(MinioController.class);

    @Autowired
    public MinioController(MinioService minioService){
        this.minioService = minioService;
    }

    @GetMapping("/buckets/exists/{bucketName}")
    public ResponseEntity<String> existsBucket(@PathVariable("bucketName") String bucketName){
        boolean found = minioService.bucketExists(bucketName);
        return new ResponseEntity<>("Bucket exists: " + found, HttpStatus.OK);
    }

    @PostMapping("/buckets/create/{bucketName}")
    public ResponseEntity<String> makeBucket(@PathVariable("bucketName") String bucketName){
        String message = minioService.makeBucket(bucketName);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/buckets/create-with-lock/{bucketName}")
    public ResponseEntity<String> makeBucketWithLockObject(@PathVariable("bucketName") String bucketName){
        String message = minioService.makeBucketWithObjectLock(bucketName);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/uploadToBucket")
    public ResponseEntity<String> uploadStringToBucket(@RequestParam String bucketName, @RequestParam String objectName){
        String message = minioService.uploadStringToBucket(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/uploadWithEncryption")
    public ResponseEntity<String> uploadStringToBucketWithS3DefaultEncryption(@RequestParam String bucketName, @RequestParam String objectName){
        String message = minioService.uploadStringToBucketWithS3DefaultEncryption(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/setBucketEncryption")
    public ResponseEntity<String> setBucketEncryption(@RequestParam String bucketName){
        String message = minioService.setBucketEncryption(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getFromBucket")
    public ResponseEntity<String> getStringFromBucket(@RequestParam String bucketName, @RequestParam String objectName){
        String content = minioService.getStringFromBucket(bucketName, objectName);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @GetMapping("/listBuckets")
    public ResponseEntity<String> listBuckets(){
        String content = minioService.listBuckets();
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

}
