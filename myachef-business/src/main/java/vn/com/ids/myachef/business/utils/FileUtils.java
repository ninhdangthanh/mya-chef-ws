package vn.com.ids.myachef.business.utils;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final long MB = 1024L * 1024L;

    public static String generatedFilename(MultipartFile file, String prefixName) {
        String filename = file.getOriginalFilename();
        String fileExt = FilenameUtils.getExtension(filename);
        return prefixName + UUID.randomUUID().toString() + "." + fileExt;
    }

    public static String generatedFilename(String prefixName) {
        return prefixName + UUID.randomUUID().toString() + "." + ".png";
    }

    public static String generatedFilename(String prefixName, String extension) {
        return prefixName + UUID.randomUUID().toString() + "." + extension;
    }

    public static long getMBSizeOfDirectory(String path) {
        long size = 0L;
        try {
            size = getSizeOfDirectory(path);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return size / MB;
    }

    private static long getSizeOfDirectory(String path) {
        File folder = new File(path);
        return org.apache.commons.io.FileUtils.sizeOfDirectory(folder);
    }

}
