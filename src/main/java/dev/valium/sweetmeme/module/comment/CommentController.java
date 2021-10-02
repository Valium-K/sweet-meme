package dev.valium.sweetmeme.module.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.InputStream;

import static dev.valium.sweetmeme.infra.config.FileConfig.ABSOLUTE_COMMENT_IMAGE_PATH;
import static dev.valium.sweetmeme.infra.config.FileConfig.COMMENT_IMAGE_URL;

/**
 * 코멘트와 코멘트 댓글관련 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    @GetMapping(COMMENT_IMAGE_URL + "{file}")
    @ResponseBody
    public ResponseEntity<byte[]> votePost(@PathVariable String file) throws Exception {

        InputStream imageStream = new FileInputStream(ABSOLUTE_COMMENT_IMAGE_PATH + file);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}
