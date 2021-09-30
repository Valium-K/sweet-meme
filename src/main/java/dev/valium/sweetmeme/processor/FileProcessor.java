package dev.valium.sweetmeme.processor;

import com.luciad.imageio.webp.WebPWriteParam;
import dev.valium.sweetmeme.config.FileConfig;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.jni.Time;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class FileProcessor {
    public static String transferFile(String path, MultipartFile file, boolean encode) throws IOException {
        String newFileName = UUID.randomUUID().toString().replace("-", "");
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());


        if ("mp4".equals(fileType)) {
            File newFile = new File(path, newFileName + "." + fileType);

            if(!newFile.exists()){
                newFile.mkdir();
            }

            file.transferTo(newFile);

            return newFile.getName();
        }
        else if("gif".equals(fileType)) {
            File newFile = new File(path, newFileName + "." + fileType);

            if(!newFile.exists()){
                newFile.mkdir();
            }

            file.transferTo(newFile);

            try {
                gif2webp(newFile);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return newFileName + ".webp";
        }
        else {
            image2webp(file,  path, newFileName);

            return newFileName + ".webp";
        }
    }

    private static void gif2webp(File file) throws IOException, InterruptedException {

        File gif2webp = ResourceUtils.getFile("classpath:static/webp/gif2webp.exe");
        String gif2webpAbsPath = gif2webp.getAbsolutePath();

        String gifAbsPath = file.getAbsolutePath();
        String webpAbsPath = gifAbsPath.substring(0, gifAbsPath.length() - 3) + "webp";

        Process process = new ProcessBuilder(gif2webpAbsPath, "-lossy", gifAbsPath, "-o", webpAbsPath).start();

        process.waitFor();
        process.destroy();
        file.delete();
    }

    private static void image2webp(MultipartFile file, String path, String fileName) throws IOException {

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
