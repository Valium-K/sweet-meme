package dev.valium.sweetmeme.module.processor.fileUploader;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    public String uploadFile(String path, MultipartFile file, boolean encode);
}
