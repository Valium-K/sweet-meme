package dev.valium.sweetmeme.infra.config;

/**
 * 경로 및 타입 상수 설정 클래스
 */
public class FileConfig {
    private static final String ABSOLUTE_PATH = "D:/sweetmeme/";

    // uploaded files
    public static final String ABSOLUTE_UPLOAD_PATH = ABSOLUTE_PATH + "image/";
    public static final String DOWNLOAD_URL = "/download/";
    public static final String FILE_URL = "/image/";
    public static final String ACCEPTABLE_FILE_TYPES = "video/mp4, video/mpeg, image/jpg, image/png, image/jpeg, image/gif, image/webp";

    // avatar files
    public static final String ABSOLUTE_AVATAR_PATH = ABSOLUTE_PATH + "profile/";
    public static final String ACCEPTABLE_AVATAR_TYPES = "image/jpg, image/jpeg, image/png, image/gif, image/webp";

    // section pics
    public static final String ABSOLUTE_SECTION_PATH = ABSOLUTE_PATH + "sections/";
    public static final String SECTION_URL = "/sections/";

    // comment image files
    public static final String ACCEPTABLE_COMMENT_TYPES = "image/jpg, image/png, image/jpeg, image/gif, image/webp";
    public static final String ABSOLUTE_COMMENT_IMAGE_PATH = ABSOLUTE_PATH + "comment/";
    public static final String COMMENT_IMAGE_URL = "/comment/";



}
