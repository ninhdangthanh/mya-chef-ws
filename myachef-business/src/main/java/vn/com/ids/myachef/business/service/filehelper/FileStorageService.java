package vn.com.ids.myachef.business.service.filehelper;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    public void upload(String uploadDir, String fileName, MultipartFile multipartFile);

    public void delete(String fileName);

    public void deleteFileAndFolder(String fileFolderDirectory, String fileDirectory);

    public boolean deleteFolder(String folder);

    public void upload(String uploadDir, String fileName, InputStream inputStream);

}
