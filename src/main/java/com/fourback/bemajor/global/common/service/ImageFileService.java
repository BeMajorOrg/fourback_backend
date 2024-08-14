package com.fourback.bemajor.global.common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageFileService {
    private static final String UPLOAD_DIR = "uploads/";

    public void deleteImageFile(String filePath) throws IOException {
        Path path = Paths.get(UPLOAD_DIR+filePath);
        Files.deleteIfExists(path);
    }

    public String saveImageFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String uniqueFileName = UUID.randomUUID() + extension;
        Files.createDirectories(Path.of(UPLOAD_DIR));
        file.transferTo(new File(UPLOAD_DIR+uniqueFileName));
        return uniqueFileName;
    }
}
