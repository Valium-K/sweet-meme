package dev.valium.sweetmeme.module.info;

import dev.valium.sweetmeme.infra.config.FileConfig;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class InfoController {

    @GetMapping("/avatar/{file}")
    public ResponseEntity<byte[]> getFile(@PathVariable String file) throws IOException {

        InputStream imageStream = new FileInputStream(FileConfig.ABSOLUTE_AVATAR_PATH + "/" + file);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}
