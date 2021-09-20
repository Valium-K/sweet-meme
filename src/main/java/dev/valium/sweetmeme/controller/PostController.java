package dev.valium.sweetmeme.controller;

import dev.valium.sweetmeme.config.FileConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.valium.sweetmeme.config.FileConfig.*;

@Controller
@RequiredArgsConstructor
public class PostController {

    @GetMapping(FILE_URL + "{file}")
    public ResponseEntity<byte[]> getFile(@PathVariable String file) throws IOException {

        InputStream imageStream = new FileInputStream(ABSOLUTE_UPLOAD_PATH + file);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    @GetMapping(DOWNLOAD_URL + "{file}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String file) {
        String path = ABSOLUTE_UPLOAD_PATH + file;

        try {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            File newFile = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(newFile.getName()).build());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(SECTION_URL + "{sectionName}")
    public ResponseEntity<byte[]> getSectionPic(@PathVariable String sectionName) throws IOException {

        InputStream imageStream = new FileInputStream(ABSOLUTE_SECTION_PATH + sectionName.toLowerCase() + ".jpg");
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}
