package dev.valium.sweetmeme.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReplyDto {
    private MultipartFile file;
    private String content;

    public ReplyDto(MultipartFile file, String content) {
        this.file = file;
        this.content = content;
    }
}
