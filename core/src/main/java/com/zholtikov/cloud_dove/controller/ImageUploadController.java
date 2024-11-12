package com.zholtikov.cloud_dove.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@RestController
@RequestMapping("/api/uploads")
@Slf4j
@Tag(name = "Images", description = "Request for uploading images to server directory, not cloud")
public class ImageUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/images")
    @SecurityRequirement(name = "BasicAuth")
    @Operation(summary = "Upload list of pictures (*.jpg or *.png) to server directory. Requires user authentication")
    public ResponseEntity<String> uploadImage(@RequestParam("file") List<MultipartFile> files) {
        StringBuilder paths = new StringBuilder();
        for (MultipartFile picture : files) {
            try {
                paths.append(saveImage(picture)).append("\n");
            } catch (IOException e) {
                log.info("Error uploading image");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
            }

        }
        log.info("Get request to endpoint POST \"/api/uploads/images\" ");
        return ResponseEntity.ok("Uploaded pictures: \n" + paths);

    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }
}
