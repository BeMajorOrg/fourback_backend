package com.fourback.bemajor.controller;


import com.fourback.bemajor.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{name}")
    public ResponseEntity<Resource> getImage(@PathVariable("name") String filename) throws IOException {
        Resource resource = imageService.get(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteImage(@RequestBody List<String> fileNames) throws IOException {
        imageService.delete(fileNames);
        return ResponseEntity.ok().build();
    }
}
