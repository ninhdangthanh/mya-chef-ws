package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.FileUploadConverter;
import vn.com.ids.myachef.business.dto.FileUploadDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.criteria.FileUploadCriteria;
import vn.com.ids.myachef.dao.model.FileUploadModel;

@RestController
@RequestMapping("/api/banners")
@Slf4j
public class BannerController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileUploadConverter fileUploadConverter;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private HttpServletRequest request;

    @Operation(summary = "Find by criteria")
    @GetMapping("/search")
    public Page<FileUploadDTO> findBycriteria(@ParameterObject FileUploadCriteria fileUploadCriteria) {
        log.info("------------------ FileUploadController - Find by criteria - START ----------------");
        fileUploadCriteria.setIsBanner(true);
        Page<FileUploadModel> page = fileUploadService.findAll(fileUploadCriteria);
        List<FileUploadDTO> fileUploadDTOs = fileUploadConverter.toBannerDTOs(page.getContent(), request);
        Pageable pageable = PageRequest.of(fileUploadCriteria.getPageIndex(), fileUploadCriteria.getPageSize());
        log.info("------------------ FileUploadController - Find by criteria - END ----------------");
        return new PageImpl<>(fileUploadDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public FileUploadDTO findbyId(@PathVariable("id") Long id) {
        log.info("------------------ FileUploadController - findById - START ----------------");
        FileUploadModel fileUploadModel = fileUploadService.findOne(id);
        if (fileUploadModel == null) {
            throw new ResourceNotFoundException(String.format("Not found fileUpload by id: %s", id));
        }
        log.info("------------------ FileUploadController - findById - END ----------------");
        return fileUploadConverter.toBannerDTO(fileUploadModel, request);
    }

    @Operation(summary = "Create Banners")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<FileUploadDTO> createBanners(@RequestParam(value = "imageFiles", required = true) List<MultipartFile> imageFiles) {
        log.info("------------------ Upload image file - Start ----------------");
        List<FileUploadDTO> bannerDTOs = fileUploadService.createBannners(imageFiles, request);
        log.info("------------------ Upload image file - End ----------------");
        return bannerDTOs;
    }

    @Operation(summary = "Delete")
    @DeleteMapping()
    public void delete(@RequestParam List<Long> ids) {
        log.info("------------------ FileUploadController - Delete - START ----------------");
        if (!CollectionUtils.isEmpty(ids)) {
            List<FileUploadModel> fileUploadModels = fileUploadService.findAllByIds(ids);
            if (!CollectionUtils.isEmpty(fileUploadModels)) {
                for (FileUploadModel fileUploadModel : fileUploadModels) {
                    fileStorageService.delete(String.format(applicationConfig.getFullBannerPath(), fileUploadModel.getGeneratedName()));
                }
                fileUploadService.deleteAll(fileUploadModels);
            }
        }
        log.info("------------------ FileUploadController - Delete - END ----------------");
    }

}
