package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.domain.CurrentMember;
import dev.valium.sweetmeme.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import static dev.valium.sweetmeme.config.FileConfig.*;

@Slf4j
@Controller
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
