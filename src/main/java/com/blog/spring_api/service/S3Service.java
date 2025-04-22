package com.blog.spring_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.DeleteObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner presigner;
    private final S3Client s3Client;


    @Value("${cloud.aws.s3.name}")
    private String bucket;

    @Value("${cloud.aws.s3.uploadPath}")
    private String path;


    public Map<String, List<String>> listDirectory(String prefix) {
        prefix = path + prefix;
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .delimiter("/") // 폴더처럼
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listRequest);

        List<String> files = response.contents().stream()
                .map(S3Object::key)
                .filter(key -> !key.endsWith("/"))
                .toList();

        List<String> folders = response.commonPrefixes().stream()
                .map(CommonPrefix::prefix)
                .toList();

        return Map.of("folders", folders, "files", files);
    }

    public String generateUploadUrl(String key, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(path + key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(request)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return presigner.presignPutObject(presignRequest).url().toString();
    }

    public String generateDownloadUrl(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentDisposition("attachment; filename=\"" + key + "\"")
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(request)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }


    public String generateDeleteUrl(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        DeleteObjectPresignRequest presignRequest = DeleteObjectPresignRequest.builder()
                .deleteObjectRequest(deleteRequest)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return presigner.presignDeleteObject(presignRequest).url().toString();
    }
}