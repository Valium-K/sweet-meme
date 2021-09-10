package dev.valium.sweetmeme.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UploadForm {

    @NotNull
    private MultipartFile file;

    @NotBlank
    @Length(min = 5, max = 50)
    private String title;
    private String tags;

    @NotBlank
    private String sections;

}
