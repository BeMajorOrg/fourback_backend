package com.fourback.bemajor.domain.aws.util;

import java.util.HashMap;
import java.util.Map;

public class ImageMimeTypeUtil {
    private static final Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("webp", "image/webp");
        mimeTypes.put("bmp", "image/bmp");
        mimeTypes.put("svg", "image/svg+xml");
    }

    public static String getImageMimeType(String extension) {
        return mimeTypes.get(extension.toLowerCase());
    }
}

