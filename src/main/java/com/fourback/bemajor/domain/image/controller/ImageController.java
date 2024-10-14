package com.fourback.bemajor.domain.image.controller;


import com.fourback.bemajor.domain.image.service.ImageService;
import com.fourback.bemajor.global.common.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{name}")
    public ResponseEntity<?> getImage(@PathVariable("name") String filename)
            throws IOException {
        Resource resource = imageService.get(filename);
        return ResponseUtil.onSuccess(resource);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteImage(@RequestBody List<String> fileNames)
            throws IOException {
        imageService.delete(fileNames);
        return ResponseUtil.onSuccess();
    }
}
