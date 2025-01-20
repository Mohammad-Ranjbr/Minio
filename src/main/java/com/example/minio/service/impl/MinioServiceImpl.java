package com.example.minio.service.impl;

import com.example.minio.service.MinioService;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.SseConfiguration;
import io.minio.messages.VersioningConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
                message = bucketName + " is created successfully";
                logger.info(message);
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + " already is exists";
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
                message = bucketName + " is created successfully";
                logger.info(message);
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + " already is exists";
        }
        return message;
    }

    @Override
    public String uploadStringToBucket(String bucketName, String objectName) {
        // In MinIO (and similar services like S3), to upload any object to the server, the data must be sent to the service as an InputStream or byte stream.
        // This means that any data you want to upload must first be converted to a stream so that MinIO can process and store it.

        // bais.available(): This method returns the number of bytes available to read from the ByteArrayInputStream.
        // In effect, bais.available() returns the number of bytes of data currently available inside the ByteArrayInputStream (i.e. the size of the data to be uploaded).
        // -1: This parameter is usually used to specify that the method should read and send until the end of the data. In effect,
        // -1 indicates that you do not want to limit the maximum number of bytes and want to send all the data. In this case, the input source (bais) will send all its data.
        String message = "";
        if(bucketExists(bucketName)){
            try{
                ByteArrayInputStream bais = new ByteArrayInputStream(createContent().toString().getBytes(StandardCharsets.UTF_8));
                minioClient.putObject(
                        PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(bais, bais.available(), -1)
                                .build());
                bais.close();
                message = objectName + " is uploaded successfully";
                logger.info(message);
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + " does not exists";
            logger.warn(message);
        }
        return message;
    }

    @Override
    public String uploadStringToBucketWithS3DefaultEncryption(String bucketName, String objectName) {
        // SSE-S3 (S3 Default Encryption): This method of encrypting data is automatically enabled in cloud storage services such as AWS S3 and MinIO,
        // and automatically uses a default internal key. For every object uploaded to these services, data is encrypted with SSE-S3 by default.
        // Encryption on upload: When you upload data, it is automatically encrypted by the storage system (S3 or MinIO). There is no need to set encryption keys and this is done by default.
        // Retrieving data: When you retrieve the data (using getObject), the storage system automatically decrypts the encrypted data,
        // and you receive understandable data. In this case, you don't need to worry about encryption because it is done transparently in the background.
        String message = "";
        if(bucketExists(bucketName)){
            try{
                ServerSideEncryption sseS3 = new ServerSideEncryptionS3();
                ByteArrayInputStream bais = new ByteArrayInputStream(createContent().toString().getBytes(StandardCharsets.UTF_8));
                minioClient.putObject(
                        PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(bais, bais.available(), -1)
                                .sse(sseS3)
                                .build());
                bais.close();
                message = objectName + " is uploaded successfully";
                logger.info(message);
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + " does not exists";
            logger.warn(message);
        }
        return message;
    }

    @Override
    public String setBucketEncryption(String bucketName) {
        // A secure connection is required to set up encryption for the bucket.
        String message = "";
        if(bucketExists(bucketName)){
            try{
                minioClient.setBucketEncryption(
                        SetBucketEncryptionArgs.builder().bucket(bucketName).config(SseConfiguration.newConfigWithSseS3Rule())
                        .build());
                message = "Encryption configuration of " + bucketName + " is set successfully";
                logger.info(message);
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + " does not exists";
            logger.warn(message);
        }
        return message;
    }

    @Override
    public String getStringFromBucket(String bucketName, String objectName) {
        // When reading data from an input source such as a file or network, it is usually not efficient to read each byte of data directly. Instead, we use a buffer, which stores the data in larger chunks (multiple bytes) and then process these chunks more efficiently.
        // When data is read from an input stream (InputStream), the data is first stored in a buffer.
        // Instead of reading one byte at a time, multiple bytes are read at once and stored in a byte array (such as byte[]).
        // We can then process the data stored in the buffer, without having to access the original data source again.
        // Improved performance: Reading or writing data in larger chunks is much faster than processing each byte individually.
        // Reduced resource accesses: There is a time delay each time we access the disk or network. Buffering data reduces the number of these accesses.
        // Better memory management: Buffers allow you to load a specific amount of data into memory and have more control over the size of the data being processed instead of using all the memory.
        // Reading data in chunks saves memory. Instead of loading the entire file at once, the file is read in small chunks, which improves efficiency and performance.
        String content = "";
        if(bucketExists(bucketName)){
            try {
                InputStream stream =
                        minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
                StringBuilder result = new StringBuilder();
                byte[] buf = new byte[16384];
                int bytesRead;
                while((bytesRead = stream.read(buf, 0, buf.length)) >= 0){
                    result.append(new String(buf, 0, bytesRead, StandardCharsets.UTF_8));
                }
                stream.close();
                content = result.toString();
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            content = bucketName + " does not exists";
            logger.warn(content);
        }
        return content;
    }

    @Override
    public String listBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            StringBuilder result = new StringBuilder();
            for(Bucket bucket:buckets){
                result.append(bucket.creationDate()).append(", ").append(bucket.name()).append("\n");
            }
            return result.toString();
        } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
        }
        return null;
    }

    @Override
    public List<String> listObjects(String bucketName) {
        List<String> objectDetails = new ArrayList<>();
        if(bucketExists(bucketName)){
            try {
                Iterable<Result<Item>> results =
                        minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
                for (Result<Item> result:results){
                    Item item = result.get();
                    String itemDetail = item.lastModified() + "     " + item.size() + "     " + item.objectName();
                    objectDetails.add(itemDetail);
                }
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            String message = bucketName + " does not exist";
            logger.warn(message);
            objectDetails.add(message);
        }
        return objectDetails;
    }

    @Override
    public String setBucketVersioning(String bucketName) {
        // Bucket Versioning is an important feature in MinIO and S3 that allows you to manage different versions of an object.
        // This feature allows you to control accidental changes or deletions in the data and revert to previous versions.
        // When Bucket Versioning is enabled, every time an object with the same name is uploaded to a bucket, a new version is created instead of being replaced.
        // Example: If a file named document.txt is uploaded to a bucket and uploaded again with the same name, the previous version is not deleted but is stored as an old version.
        // Protection against accidental deletion of data:
        // With versioning enabled, deleting an object does not mean deleting all versions. Rather,
        // a Delete Marker is added to indicate the deletion of the object, but the previous versions are still available unless you delete specific versions.
        String message = "";
        if(bucketExists(bucketName)){
            try {
                minioClient.setBucketVersioning(
                        SetBucketVersioningArgs.builder().bucket(bucketName)
                                .config(new VersioningConfiguration(VersioningConfiguration.Status.ENABLED, null))
                                .build());
                message = "Bucket versioning is enabled successfully for " + bucketName;
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + " does not exists";
            logger.warn(message);
        }
        return message;
    }

    @Override
    public String getBucketVersioning(String bucketName) {
        String message = "";
        if(bucketExists(bucketName)){
            try {
                VersioningConfiguration config = minioClient.getBucketVersioning(
                    GetBucketVersioningArgs.builder().bucket(bucketName).build());
                message = config.status().toString();
            } catch (MinioException | IOException | NoSuchAlgorithmException |  InvalidKeyException exception){
                logger.error("Error occurred: " + exception);
            }
        } else {
            message = bucketName + " does not exists";
            logger.warn(message);
        }
        return message;
    }

    private StringBuilder createContent(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append(
                    "Sphinx of black quartz, judge my vow: Used by Adobe InDesign to display font samples. ");
            builder.append("(29 letters)\n");
            builder.append(
                    "Jackdaws love my big sphinx of quartz: Similarly, used by Windows XP for some fonts. ");
            builder.append("(31 letters)\n");
            builder.append(
                    "Pack my box with five dozen liquor jugs: According to Wikipedia, this one is used on ");
            builder.append("NASAs Space Shuttle. (32 letters)\n");
            builder.append(
                    "The quick onyx goblin jumps over the lazy dwarf: Flavor text from an Unhinged Magic Card. ");
            builder.append("(39 letters)\n");
            builder.append(
                    "How razorback-jumping frogs can level six piqued gymnasts!: Not going to win any brevity ");
            builder.append("awards at 49 letters long, but old-time Mac users may recognize it.\n");
            builder.append(
                    "Cozy lummox gives smart squid who asks for job pen: A 41-letter tester sentence for Mac ");
            builder.append("computers after System 7.\n");
            builder.append(
                    "A few others we like: Amazingly few discotheques provide jukeboxes; Now fax quiz Jack! my ");
            builder.append("brave ghost plied; Watch Jeopardy!, Alex Treks fun TV quiz game.\n");
            builder.append("---\n");
        }
        return builder;
    }

}
