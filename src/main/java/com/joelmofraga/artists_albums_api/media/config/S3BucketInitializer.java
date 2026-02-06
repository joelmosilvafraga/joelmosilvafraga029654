package com.joelmofraga.artists_albums_api.media.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.S3Exception;


@Slf4j
@Configuration
public class S3BucketInitializer {

    @Bean
    public ApplicationRunner ensureBucketExists(
            S3Client s3,
            @Value("${storage.s3.bucket}") String bucket
    ) {
        return args -> {
            try {
                s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
                log.info("S3 bucket OK: {}", bucket);
            } catch (NoSuchBucketException e) {
                log.warn("S3 bucket does not exist, creating: {}", bucket);
                s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
                log.info("S3 bucket created: {}", bucket);
            } catch (S3Exception e) {
                if (e.statusCode() == 404) {
                    log.warn("S3 bucket not found (404), creating: {}", bucket);
                    s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
                    log.info("S3 bucket created: {}", bucket);
                } else {
                    log.error("Failed to validate/create bucket {}: {}", bucket, e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage());
                    throw e;
                }
            }
        };
    }
}
