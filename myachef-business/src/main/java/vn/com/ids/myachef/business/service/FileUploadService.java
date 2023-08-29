package vn.com.ids.myachef.business.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.FileUploadDTO;
import vn.com.ids.myachef.dao.criteria.FileUploadCriteria;
import vn.com.ids.myachef.dao.enums.BannerType;
import vn.com.ids.myachef.dao.model.FileUploadModel;

public interface FileUploadService extends IGenericService<FileUploadModel, Long> {

    public Page<FileUploadModel> findAll(FileUploadCriteria fileUploadCriteria);

    public String getFilePath(String path, Long modelId, String filename, HttpServletRequest request);

    public List<FileUploadModel> findAllByIds(List<Long> ids);

    public List<FileUploadModel> uploadFiles(List<MultipartFile> files, Object object);

    public void deleteAll(List<FileUploadModel> fileUploadModels);

    public List<FileUploadDTO> createBannners(List<MultipartFile> imageFiles, HttpServletRequest request);

    public String getFilePath(String path, String filename, HttpServletRequest request);

    public void deleteBannerModelAndFileById(Long id);

    public FileUploadModel findByBannerTypeAndIsBannerTrue(BannerType type);
}
