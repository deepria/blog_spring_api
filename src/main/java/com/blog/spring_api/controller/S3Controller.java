package com.blog.spring_api.controller;

import com.blog.spring_api.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/list")
    public ResponseEntity<Map<String, List<String>>> listFiles(@RequestParam(defaultValue = "") String prefix) {
        Map<String, List<String>> result = s3Service.listDirectory(prefix);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/upload-url")
    public ResponseEntity<String> getUploadUrl(
            @RequestParam String filename,
            @RequestParam String contentType) {
        String url = s3Service.generateUploadUrl(filename, contentType);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/download-url")
    public ResponseEntity<String> getDownloadUrl(@RequestParam String filename) {
        String url = s3Service.generateDownloadUrl(filename);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/delete-url")
    public ResponseEntity<String> getDeleteUrl(@RequestParam String filename) {
        String url = s3Service.generateDeleteUrl(filename);
        return ResponseEntity.ok(url);
    }


}
