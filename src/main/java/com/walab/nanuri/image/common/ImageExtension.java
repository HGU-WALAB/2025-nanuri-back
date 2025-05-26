package com.walab.nanuri.image.common;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ImageExtension {
    PNG("png", "image/png", 1),
    JPG("jpg", "image/jpeg", 2),
    JPEG("jpeg", "image/jpeg", 2),
    SVG("svg", "image/svg+xml", 3);

    private final String extension;
    private final String mimeType;
    private final Integer type;

    ImageExtension(String extension, String mimeType, Integer type) {
        this.extension = extension;
        this.mimeType = mimeType;
        this.type = type;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Integer getType() {
        return type;
    }

    public static final Set<String> ALLOWED_EXTENSIONS = Stream.of(ImageExtension.values())
            .map(ImageExtension::getExtension)
            .collect(Collectors.toSet());

    public static boolean isValidExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }


    public static String getMimeType(ImageExtension type) {
        switch (type) {
            case PNG:
                return "image/png";
            case JPG:
            case JPEG:
                return "image/jpeg";
            case SVG:
                return "image/svg+xml";
            default:
                return "application/octet-stream"; // 기본값
        }
    }
};