package com.fourback.bemajor.global.common.response;

import com.fourback.bemajor.global.exception.ExceptionBody;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    public static ResponseEntity<?> onSuccess() {
        return ResponseEntity.ok().build();
    }

    public static ResponseEntity<?> onSuccess(HttpHeaders headers) {
        return ResponseEntity.ok().headers(headers).build();
    }

    public static <T> ResponseEntity<T> onSuccess(HttpHeaders headers, T body) {
        return ResponseEntity.ok().headers(headers).body(body);
    }

    public static <T> ResponseEntity<T> onSuccess(T body) {
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<ExceptionBody> onFailed(
            HttpStatusCode httpStatusCode, ExceptionBody body) {
        return ResponseEntity.status(httpStatusCode).body(body);
    }

    public static HttpHeaders createContentDispositionHeader(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION
                , "inline; filename=\"" + filename + "\"");
        return headers;
    }

    public static HttpHeaders createHeaders(List<Pair<String, String>> pairs) {
        HttpHeaders headers = new HttpHeaders();
        for (Pair<String, String> pair : pairs) {
            headers.add(pair.getLeft(), pair.getRight());
        }
        return headers;
    }
}
