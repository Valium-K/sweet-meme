package dev.valium.sweetmeme.module.post.form;

import dev.valium.sweetmeme.infra.config.FileConfig;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UploadForm {
    public final String ACCEPTABLE_FILE_TYPES = FileConfig.ACCEPTABLE_FILE_TYPES;

    @NotNull
    private MultipartFile file;

    @NotBlank
    @Length(min = 5, max = 50, message = "{upload.title.help}")
    private String title;

    @Length(max = 100)
    private String tags;

    @NotBlank
    private String sections;
}
