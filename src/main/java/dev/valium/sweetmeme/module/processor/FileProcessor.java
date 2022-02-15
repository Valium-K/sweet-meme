package dev.valium.sweetmeme.module.processor;

import com.luciad.imageio.webp.WebPWriteParam;
import dev.valium.sweetmeme.module.processor.fileUploader.FileUploader;
import dev.valium.sweetmeme.module.processor.fileUploader.FileUploaderFactory;
import dev.valium.sweetmeme.module.processor.fileUploader.FileUploaderLinux;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class FileProcessor {

    public static String transferFile(String path, MultipartFile file, boolean encode) throws IOException {
        String activeEnv = EnvProcessor.getActiveProfile();
        FileUploader fileUploader = FileUploaderFactory.getInstance(activeEnv);

        // It returns file name.
        // 현제 인코딩 옵션은 강제 True
        return fileUploader.uploadFile(path, file, encode);
    }
}
