package vn.com.ids.myachef.business.service.filehelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    public void upload(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("--- Upload File: {} successful!", fileName);
        } catch (IOException ex) {
            log.error("--- Could not upload File: {}", fileName);
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void upload(String uploadDir, String fileName, InputStream inputStream) {
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("--- Upload File: {} successful!", fileName);
        } catch (IOException ex) {
            log.error("--- Could not upload File: {}", fileName);
            log.error(ex.getMessage(), ex);
        }
    }

    public void delete(String filename) {
        try {
            boolean result = Files.deleteIfExists(Paths.get(filename));
            log.info("Delete the file {} {}", filename, result ? " successful!" : " failed!");
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public boolean deleteFolder(String folder) {
        if (!StringUtils.hasText(folder) || !Files.exists(Paths.get(folder))) {
            log.info("--- The folder '{}' is null or incorrect!", folder);
            return false;
        }
        boolean isSuccessful = true;
        try {
            FileUtils.deleteDirectory(new File(folder));
        } catch (Exception ex) {
            isSuccessful = false;
            log.error("---Exception: Delete the folder {} failed!", folder);
            log.error(ex.getMessage(), ex);
        }
        log.info("Delete the folder {} {}", folder, isSuccessful ? " successful!" : " failed!");
        return isSuccessful;
    }

    /**
     * Delete file and its folder, set fileFolderDirectory = null in case file doesn't separate folder
     */
    public void deleteFileAndFolder(String folder, String file) {
        try {
            Path filePath = Paths.get(file);
            Files.delete(filePath);
            if (folder != null) {
                Path fileFolderPath = Paths.get(folder);
                Files.delete(fileFolderPath);
            }
            log.info("--- DELETE File and Folder successfull!");
        } catch (IOException ex) {
            log.error("{}", ex.getMessage());
            log.error(ex.getMessage(), ex);
        }
    }

}
