package com.fourback.bemajor.global.common.util;

import com.fourback.bemajor.global.exception.ExceptionDto;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ResponseUtil {
    public static ResponseEntity<?> onSuccess() {
        return ResponseEntity.ok().build();
    }

    public static ResponseEntity<?> onSuccess(List<Pair<String, String>> pairs) {
        HttpHeaders headers = new HttpHeaders();
        pairs.forEach(pair -> headers.add(pair.getLeft(), pair.getRight()));
        return ResponseEntity.ok().headers(headers).build();
    }

    public static ResponseEntity<?> onSuccess(Resource resource) {
        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    public static <T> ResponseEntity<T> onSuccess(T body) {
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<ExceptionDto> onFailed(
            HttpStatusCode httpStatusCode, ExceptionDto body) {
        return ResponseEntity.status(httpStatusCode).body(body);
    }

}
