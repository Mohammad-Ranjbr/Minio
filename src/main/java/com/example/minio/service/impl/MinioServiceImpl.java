package com.example.minio.service.impl;

import com.example.minio.service.MinioService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
