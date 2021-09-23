package dev.valium.sweetmeme.processor;

import com.luciad.imageio.webp.WebPWriteParam;
import org.apache.commons.io.FilenameUtils;
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

public class FileProcessor {
    public static String transferFile(String path, MultipartFile file, boolean encode) throws IOException {
        String newFileName = UUID.randomUUID().toString().replace("-", "");
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());

        if(encode && !"mp4".equals(fileType)) {
            image2webp(file,  path, newFileName);

            return newFileName + ".webp";
        }
        else {
            File newFile = new File(path, newFileName + "." + fileType);

            if(!newFile.exists()){
                newFile.mkdir();
            }
            file.transferTo(newFile);

            return newFile.getName();
        }
    }
    public static void image2webp(MultipartFile file, String path, String fileName) throws IOException {

        // BufferedImage read = ImageIO.read(file.getInputStream());
        File newFile = new File(path, fileName + ".webp");

        // Obtain an image to encode from somewhere
        BufferedImage image = ImageIO.read(file.getInputStream());

        // Obtain a WebP ImageWriter instance
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

        // Configure encoding parameters
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);

        // Configure the output on the ImageWriter
        writer.setOutput(new FileImageOutputStream(newFile));

        // Encode
        writer.write(null, new IIOImage(image, null, null), writeParam);
    }
}
