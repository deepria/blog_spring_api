package com.blog.spring_api.controller;

import com.blog.spring_api.entity.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FileController {

    private final Path rootLocation = Paths.get("uploads"); // 파일 저장 경로

    // 디렉터리 목록 조회
    @GetMapping("/directory")
    public ResponseEntity<List<FileEntity>> listDirectory() {
        try {
            List<FileEntity> files = Files.walk(rootLocation, 1)
                    .filter(Files::isRegularFile)
                    .map(path -> new FileEntity(path.getFileName().toString(), path.toAbsolutePath().toString()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> handleFileDownload(
            @RequestParam("path") String path,
            @RequestParam("key") String authKey) {
        try {
            // 파일 경로 설정 및 유효성 확인
            Path file = rootLocation.resolve(path).normalize();
            if (!Files.exists(file)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 파일 이름 및 MIME 타입 설정
            String fileName = file.getFileName().toString();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 리소스 생성
            Resource resource = new UrlResource(file.toUri());

            // 응답 생성
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"default-name\"; filename*=UTF-8''" + encodedFileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            Path destinationFile = rootLocation.resolve(
                    Paths.get(Objects.requireNonNull(file.getOriginalFilename()))).normalize().toAbsolutePath();
            file.transferTo(destinationFile);
            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("path") String path) {
        try {
            // 삭제 대상 파일 경로 확인
            Path file = rootLocation.resolve(path).normalize();
            if (!Files.exists(file)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }
            // 파일 삭제
            Files.delete(file);
            return ResponseEntity.ok("File deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete file: " + e.getMessage());
        }
    }
}
