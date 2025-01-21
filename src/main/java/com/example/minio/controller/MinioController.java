package com.example.minio.controller;

import com.example.minio.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/minio")
public class MinioController {

    private final MinioService minioService;

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
        String message = minioService.uploadObjectToBucket(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/uploadWithEncryption")
    public ResponseEntity<String> uploadStringToBucketWithS3DefaultEncryption(@RequestParam String bucketName, @RequestParam String objectName){
        String message = minioService.uploadObjectToBucketWithS3DefaultEncryption(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/setBucketEncryption")
    public ResponseEntity<String> setBucketEncryption(@RequestParam String bucketName){
        String message = minioService.setBucketEncryption(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getFromBucket")
    public ResponseEntity<String> getStringFromBucket(@RequestParam String bucketName, @RequestParam String objectName){
        String content = minioService.getObjectFromBucket(bucketName, objectName);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @GetMapping("/buckets/lists")
    public ResponseEntity<String> listBuckets(){
        String content = minioService.listBuckets();
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @GetMapping("/listObjects/{bucketName}")
    public ResponseEntity<List<String>> listObjects(@PathVariable("bucketName") String bucketName){
        List<String> content = minioService.listObjects(bucketName);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @PostMapping("/buckets/setVersioning/{bucketName}")
    public ResponseEntity<String> setBucketVersioning(@PathVariable("bucketName") String bucketName){
        String message = minioService.setBucketVersioning(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/buckets/getVersioning/{bucketName}")
    public ResponseEntity<String> getBucketVersioning(@PathVariable("bucketName") String bucketName){
        String message = minioService.getBucketVersioning(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/buckets/{bucket-name}/Object/{object-name}/versions")
    public ResponseEntity<String> getObjectVersions(@PathVariable("bucket-name") String bucketName, @PathVariable("object-name") String objectName){
        String result = minioService.getObjectVersions(bucketName, objectName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/buckets/{bucket-name}/objects/{object-name}/versions/{version-id}")
    public ResponseEntity<String> getObjectByVersion(@PathVariable("bucket-name") String bucketName,
                                                     @PathVariable("object-name") String objectName, @PathVariable("version-id") String versionId){
        String result = minioService.getObjectByVersion(bucketName, objectName, versionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/buckets/{bucket-name}/lifeCycle")
    public ResponseEntity<String> getBucketLifecycle(@PathVariable("bucket-name") String bucketName){
        String result = minioService.getBucketLifecycle(bucketName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/buckets/{bucket-name}/setTags")
    public ResponseEntity<String> setBucketTags(@PathVariable("bucket-name") String bucketName){
        String message = minioService.setBucketTags(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/buckets/{bucket-name}/getTags")
    public ResponseEntity<String> getBucketTags(@PathVariable("bucket-name") String bucketName){
        String result = minioService.getBucketTags(bucketName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
