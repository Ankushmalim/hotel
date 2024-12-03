package com.hms.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${bucket.name}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(File file) {
        String key = "uploads/" + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        return amazonS3.getUrl(bucketName, key).toString(); // Public URL of the uploaded file
    }
}

