package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.FileUploadDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.model.FileUploadModel;

@Component
public class FileUploadConverter {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ModelMapper mapper;

    public FileUploadDTO toBasicDTO(FileUploadModel fileUploadModel, HttpServletRequest request) {
        FileUploadDTO fileUploadDTO = mapper.map(fileUploadModel, FileUploadDTO.class);
        fileUploadDTO.setPath(
                fileUploadService.getFilePath(applicationConfig.getFileUpload(), fileUploadModel.getId(), fileUploadModel.getGeneratedName(), request));

        if (StringUtils.hasText(fileUploadDTO.getThumbnail())) {
            fileUploadDTO.setThumbnailPath(
                    fileUploadService.getFilePath(applicationConfig.getFileUpload(), fileUploadModel.getId(), fileUploadModel.getThumbnail(), request));
        }
        return fileUploadDTO;
    }

    public FileUploadDTO toBannerDTO(FileUploadModel fileUploadModel, HttpServletRequest request) {
        FileUploadDTO fileUploadDTO = mapper.map(fileUploadModel, FileUploadDTO.class);
        fileUploadDTO.setPath(fileUploadService.getFilePath(applicationConfig.getBannerPath(), fileUploadModel.getGeneratedName(), request));
        return fileUploadDTO;
    }

    public List<FileUploadDTO> toBannerDTOs(List<FileUploadModel> fileUploadModels, HttpServletRequest request) {
        List<FileUploadDTO> fileUploadDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileUploadModels)) {
            for (FileUploadModel fileUploadModel : fileUploadModels) {
                fileUploadDTOs.add(toBannerDTO(fileUploadModel, request));
            }
        }
        return fileUploadDTOs;
    }

    public List<FileUploadDTO> toBasicDTOs(List<FileUploadModel> fileUploadModels, HttpServletRequest request) {
        List<FileUploadDTO> fileUploadDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileUploadModels)) {
            for (FileUploadModel fileUploadModel : fileUploadModels) {
                fileUploadDTOs.add(toBasicDTO(fileUploadModel, request));
            }
        }
        return fileUploadDTOs;
    }

    public FileUploadDTO toDTO(FileUploadModel fileUploadModel, HttpServletRequest request) {
        return toBasicDTO(fileUploadModel, request);
    }

    public List<FileUploadDTO> toDTOs(List<FileUploadModel> fileUploadModels, HttpServletRequest request) {
        List<FileUploadDTO> fileUploadDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileUploadModels)) {
            for (FileUploadModel fileUploadModel : fileUploadModels) {
                fileUploadDTOs.add(toDTO(fileUploadModel, request));
            }
        }

        return fileUploadDTOs;
    }

    public FileUploadModel toModel(FileUploadDTO fileUploadDTO) {
        FileUploadModel fileUploadModel = mapper.map(fileUploadDTO, FileUploadModel.class);
        return fileUploadModel;
    }

    public List<FileUploadDTO> toDTOs(List<FileUploadModel> fileUploads, String filePath, HttpServletRequest request) {
        List<FileUploadDTO> fileUploadDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileUploads)) {
            for (FileUploadModel fileUploadModel : fileUploads) {
                fileUploadDTOs.add(toDTO(fileUploadModel, filePath, request));
            }
        }

        return fileUploadDTOs;
    }

    public FileUploadDTO toDTO(FileUploadModel fileUploadModel, String filePath, HttpServletRequest request) {
        if (fileUploadModel != null) {
            FileUploadDTO fileUploadDTO = mapper.map(fileUploadModel, FileUploadDTO.class);
            fileUploadDTO.setPath(fileUploadService.getFilePath(filePath, fileUploadModel.getGeneratedName(), request));

            if (StringUtils.hasText(fileUploadDTO.getThumbnail())) {
                fileUploadDTO.setThumbnailPath(fileUploadService.getFilePath(filePath, fileUploadModel.getThumbnail(), request));
            }

            return fileUploadDTO;
        }
        return null;
    }
}
