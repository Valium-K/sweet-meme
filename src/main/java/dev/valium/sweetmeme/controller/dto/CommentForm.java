package dev.valium.sweetmeme.controller.dto;

import dev.valium.sweetmeme.config.FileConfig;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentForm {

    public final String ACCEPTABLE_FILE_TYPES = FileConfig.ACCEPTABLE_FILE_TYPES;

    @NotNull
    private MultipartFile file;

    @NotBlank
    @Length(min = 2, max = 100)
    private String content;
}
