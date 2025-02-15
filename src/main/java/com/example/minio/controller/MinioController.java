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

    @GetMapping("/buckets/{bucket-name}/exists")
    public ResponseEntity<String> existsBucket(@PathVariable("bucket-name") String bucketName){
        boolean found = minioService.bucketExists(bucketName);
        return new ResponseEntity<>("Bucket exists: " + found, HttpStatus.OK);
    }

    @PostMapping("/buckets/{bucket-name}/create")
    public ResponseEntity<String> makeBucket(@PathVariable("bucket-name") String bucketName){
        String message = minioService.makeBucket(bucketName);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/buckets/{bucket-name}/createWithLock")
    public ResponseEntity<String> makeBucketWithLockObject(@PathVariable("bucket-name") String bucketName){
        String message = minioService.makeBucketWithObjectLock(bucketName);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/objects/upload")
    public ResponseEntity<String> uploadStringToBucket(@RequestParam String bucketName, @RequestParam String objectName){
        String message = minioService.uploadObjectToBucket(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/objects/uploadWithEncryption")
    public ResponseEntity<String> uploadStringToBucketWithS3DefaultEncryption(@RequestParam String bucketName, @RequestParam String objectName){
        String message = minioService.uploadObjectToBucketWithS3DefaultEncryption(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/buckets/setEncryption")
    public ResponseEntity<String> setBucketEncryption(@RequestParam String bucketName){
        String message = minioService.setBucketEncryption(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/object/getObject")
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

    @DeleteMapping("/buckets/{bucket-name}/deleteTags")
    public ResponseEntity<String> deleteBucketTags(@PathVariable("bucket-name") String bucketName){
        String message = minioService.deleteBucketTags(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/buckets/{bucket-name}/objects/{object-name}/setTags")
    public ResponseEntity<String> setObjectTags(@PathVariable("bucket-name") String bucketName, @PathVariable("object-name") String objectName){
        String message = minioService.setObjectTags(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/buckets/{bucket-name}/objects/{object-name}/getTags")
    public ResponseEntity<String> getObjectTags(@PathVariable("bucket-name") String bucketName, @PathVariable("object-name") String objectName){
        String result = minioService.getObjectTags(bucketName, objectName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/buckets/{bucket-name}/objects/{object-name}/deleteTags")
    public ResponseEntity<String> deleteObjectTags(@PathVariable("bucket-name") String bucketName, @PathVariable("object-name") String objectName){
        String message = minioService.deleteObjectTags(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/buckets/{bucket-name}/remove")
    public ResponseEntity<String> removeBucket(@PathVariable("bucket-name") String bucketName){
        String message = minioService.removeBucket(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/buckets/{bucket-name}/objects/{object-name}/remove")
    public ResponseEntity<String> removeObject(@PathVariable("bucket-name") String bucketName, @PathVariable("object-name")String objectName){
        String message = minioService.removeObject(bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/buckets/{bucket-name}/objects/removeObjects")
    public ResponseEntity<String> removeObjects(@PathVariable("bucket-name") String bucketName){
        String message = minioService.removeObjects(bucketName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/objects/copy")
    public ResponseEntity<String> copyObject(@RequestParam String sourceBucketName, @RequestParam String sourceObjectName,
                                             @RequestParam String bucketName, @RequestParam String objectName){
        String message = minioService.copyObject(sourceBucketName, sourceObjectName, bucketName, objectName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
