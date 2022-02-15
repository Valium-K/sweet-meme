package dev.valium.sweetmeme.module.processor.fileUploader;

public class FileUploaderFactory {
    private static FileUploader fileUploader = null;
    public static FileUploader getInstance(String activeProfile) {
        if (fileUploader != null) {
            return fileUploader;
        }

        if ("linux".equals(activeProfile)) {
            return new FileUploaderLinux();
        }
        else {
            return new FileUploaderDefault();
        }
    }
}
