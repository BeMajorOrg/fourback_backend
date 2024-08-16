package com.fourback.bemajor.domain.image.controller;


import com.fourback.bemajor.domain.image.service.ImageService;
import com.fourback.bemajor.global.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.fourback.bemajor.global.common.response.Response.createContentDispositionHeader;


@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{name}")
    public ResponseEntity<Resource> getImage(@PathVariable("name") String filename)
            throws IOException {
        Resource resource = imageService.get(filename);
        return Response.onSuccess(createContentDispositionHeader(resource.getFilename()),
                                  resource);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteImage(@RequestBody List<String> fileNames)
            throws IOException {
        imageService.delete(fileNames);
        return Response.onSuccess();
    }
}
