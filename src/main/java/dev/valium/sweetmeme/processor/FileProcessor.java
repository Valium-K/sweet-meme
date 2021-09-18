package dev.valium.sweetmeme.processor;

import com.zakgof.webp4j.Webp4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileProcessor {
    public static File createNewFile(String path, MultipartFile file, boolean encode) throws IOException {
        String newFileName = UUID.randomUUID().toString().replace("-", "");
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        File newFile;

        if(encode && !"mp4".equals(fileType)) {
            newFile = image2webp(file, 80f, path, newFileName);
        } else {
            newFile = new File(path, newFileName + "." + fileType);
        }

        if(!newFile.exists()){
            newFile.mkdir();
        }

        return newFile;
    }
    public static File image2webp(MultipartFile file, float quality, String path, String fileName) throws IOException {
        BufferedImage read = ImageIO.read(file.getInputStream());

        byte[] bytes = Webp4j.encode(read, quality);

        File newFile = new File(path, fileName + ".webp");
        FileOutputStream lFileOutputStream = new FileOutputStream(newFile);
        lFileOutputStream.write(bytes);
        lFileOutputStream.close();

        return newFile;
    }
}
