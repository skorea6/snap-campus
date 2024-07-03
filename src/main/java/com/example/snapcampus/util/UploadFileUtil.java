package com.example.snapcampus.util;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtil {
    public static void checkFileIsImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB 이상이면 에러 반환
            throw new IllegalArgumentException("최대 5MB 파일까지만 업로드가 가능합니다.");
        }

        String[] allowedTypes = {"image/png", "image/jpg", "image/gif", "image/jpeg"};
        boolean isAllowed = false;
        for (String type : allowedTypes) {
            if (type.equals(file.getContentType())) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw new IllegalArgumentException("png, jpg, gif, jpeg 파일만 업로드가 가능합니다.");
        }
    }
}
