package com.example.minio.config.swagger;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Minio Application",
                description = "This API provides functionalities to interact with MinIO, allowing users to manage object storage. It includes features for uploading," +
                        " downloading, and organizing files within buckets. The API also integrates authentication and authorization mechanisms for secure access to MinIO resources.",
                version = "v1.0",
                contact = @Contact(name = "Mohammad Ranjbar", email = "mohammadranjbar.mmr81@gmail.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        externalDocs = @ExternalDocumentation(description = "For more details, visit the GitHub repository",
                url = "https://github.com/Mohammad-Ranjbr/Minio")
)
public class SwaggerConfig {
}
