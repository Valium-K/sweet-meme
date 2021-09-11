package dev.valium.sweetmeme.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UploadForm {
    public final String ACCEPTABLE_FILE_TYPES = "video/mpeg, video/mp4, image/png, image/jpeg, image/gif";

    @NotNull
    private MultipartFile file;

    @NotBlank
    @Length(min = 5, max = 50)
    private String title;
    private String tags;

    @NotBlank
    private String sections;



}
