package com.example.snapcampus.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file, String path, String fileName) throws IOException {
        String newFileName = getNewFileName(file, fileName);
        InputStream fileInputStream = file.getInputStream();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(bucketName, path + newFileName, fileInputStream, metadata);
        fileInputStream.close();  // InputStream 을 명시적으로 닫아주는 것이 좋음

        return newFileName;
    }

    private static String getNewFileName(MultipartFile file, String fileName) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 비어있습니다.");
        }

        String fileExtension = originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1)
                : "";

        if (fileExtension.isEmpty()) {
            throw new IllegalArgumentException("파일 확장자가 올바르지 않습니다.");
        }

        return fileName + "." + fileExtension;
    }

    public void deleteFile(String fullPath) {
        amazonS3.deleteObject(bucketName, fullPath);
    }
}