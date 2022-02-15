package dev.valium.sweetmeme.module.processor.fileUploader;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploaderLinux implements FileUploader {
    @Override
    public String uploadFile(String path, MultipartFile file, boolean encode) {
        // TODO Linux용 webp 모듈 찾아 적용하기
        String newFileName = UUID.randomUUID().toString().replace("-", "");
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());

        // 현재 생짜 저장
        File newFile = new File(path, newFileName + "." + fileType);

        if(!newFile.exists()){
            newFile.mkdir();
        }

        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            // TODO custom exception 추가
            e.printStackTrace();
        }

        return newFile.getName();
    }
}
