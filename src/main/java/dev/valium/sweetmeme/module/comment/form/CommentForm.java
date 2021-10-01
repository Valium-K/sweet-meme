package dev.valium.sweetmeme.module.comment.form;

import dev.valium.sweetmeme.infra.config.FileConfig;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CommentForm {

    public final String ACCEPTABLE_COMMENT_TYPES = FileConfig.ACCEPTABLE_COMMENT_TYPES;

    private MultipartFile file;

    @Length(max = 100)
    private String content;

}
