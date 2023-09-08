package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.FileUploadConverter;
import vn.com.ids.myachef.business.dto.FileUploadDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.business.utils.FileUtils;
import vn.com.ids.myachef.business.utils.RequestUtils;
import vn.com.ids.myachef.dao.criteria.FileUploadCriteria;
import vn.com.ids.myachef.dao.criteria.builder.FileUploadSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.BannerType;
import vn.com.ids.myachef.dao.model.FileUploadModel;
import vn.com.ids.myachef.dao.repository.FileUploadRepository;

@Service
@Transactional
public class FileUploadServiceImpl extends AbstractService<FileUploadModel, Long> implements FileUploadService {

    private FileUploadRepository fileUploadRepository;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileUploadConverter fileUploadConverter;
    
    @Autowired
    private HttpServletRequest request;

    protected FileUploadServiceImpl(FileUploadRepository fileUploadRepository) {
        super(fileUploadRepository);
        this.fileUploadRepository = fileUploadRepository;
    }

    public Specification<FileUploadModel> buildSpecification(FileUploadCriteria fileUploadCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new FileUploadSpecificationBuilder(root, criteriaBuilder) //
                .setGeneratedName(fileUploadCriteria.getGeneratedName()) //
                .setOriginalName(fileUploadCriteria.getOriginalName()) //
                .setBanner(fileUploadCriteria.getIsBanner())//
                .setTimeFrame(fileUploadCriteria.getFrom(), fileUploadCriteria.getTo())//
                .build();
    }

    @Override
    public List<FileUploadModel> findAllByIds(List<Long> ids) {
        return fileUploadRepository.findAllById(ids);
    }

    @Override
    public Page<FileUploadModel> findAll(FileUploadCriteria fileUploadCriteria) {
        Specification<FileUploadModel> specification = buildSpecification(fileUploadCriteria);
        Pageable pageable = buildPageable(fileUploadCriteria);
        return fileUploadRepository.findAll(specification, pageable);
    }

    @Override
    public List<FileUploadModel> uploadFiles(List<MultipartFile> files, Object parent) {
        return updateDatas(files, parent);
    }

    private List<FileUploadModel> updateDatas(List<MultipartFile> files, Object parent) {
        List<FileUploadModel> fileUploadModels = new ArrayList<>();
        // FileUploadType type = null;
        Long parentId = null;
        String prefixName = null;
        // if (parent instanceof ProductModel) {
        // ProductModel product = (ProductModel) parent;
        // prefixName = "product_attachment_";
        // type = FileUploadType.PRODUCT;
        // parentId = product.getId();
        // } else if (parent instanceof ProductGuidelineModel) {
        // ProductGuidelineModel productGuideline = (ProductGuidelineModel) parent;
        // prefixName = "product_guideline_attachment_";
        // type = FileUploadType.PRODUCT_GUIDELINE;
        // parentId = productGuideline.getId();
        // } else if (parent instanceof ProductReviewModel) {
        // ProductReviewModel productReview = (ProductReviewModel) parent;
        // prefixName = "product_guideline_attachment_";
        // type = FileUploadType.PRODUCT_REVIEW;
        // parentId = productReview.getId();
        // }

        for (MultipartFile file : files) {
            FileUploadModel fileUploadModel = new FileUploadModel();
            fileUploadModel.setMimeType(file.getContentType());
            fileUploadModel.setOriginalName(file.getOriginalFilename());
            fileUploadModel.setSize(file.getSize());
            // if (parent instanceof ProductModel) {
            // fileUploadModel.setProduct((ProductModel) parent);
            // } else if (parent instanceof ProductGuidelineModel) {
            // fileUploadModel.setProductGuideline((ProductGuidelineModel) parent);
            // } else if (parent instanceof ProductReviewModel) {
            // fileUploadModel.setProductReview((ProductReviewModel) parent);
            // }
            // String filePath = getFilePathByType(type);
            String generatedName = FileUtils.generatedFilename(file, prefixName);
            // String fullFilePath = String.format(filePath, parentId);
            // fileStorageService.upload(fullFilePath, generatedName, file);
            fileUploadModel.setGeneratedName(generatedName);
            fileUploadModels.add(fileUploadModel);
        }

        fileUploadRepository.saveAll(fileUploadModels);

        return fileUploadModels;
    }

    // @Override
    // public long deleteFiles(List<FileUploadModel> fileUploadModels, Long modelId, FileUploadType type) {
    // String filePath = getFilePathByType(type);
    // long totalFileChange = 0l;
    // if (fileUploadModels != null) {
    // for (FileUploadModel fileUploadModel : fileUploadModels) {
    // String fileName = String.format(filePath, modelId) + fileUploadModel.getGeneratedName();
    // fileStorageService.delete(fileName);
    // totalFileChange += fileUploadModel.getSize();
    // }
    // }

    @Override
    public String getFilePath(String path, Long modelId, String filename, HttpServletRequest request) {
        String filePath = null;
        if (filename != null) {
            String serverInfo = RequestUtils.getServerInfo(request);
            filePath = String.format("%s/data/%s%s/%s", serverInfo, path, modelId, filename);
        }
        return filePath;
    }

    @Override
    public String getFilePath(String path, String filename, HttpServletRequest request) {
        String filePath = null;
        if (filename != null) {
            String serverInfo = RequestUtils.getServerInfo(request);
            filePath = String.format("%s/data/%s%s", serverInfo, path, filename);
        }
        return filePath;
    }

    @Override
    public void deleteAll(List<FileUploadModel> fileUploadModels) {
        fileUploadRepository.deleteAll(fileUploadModels);
    }

    @Override
    public List<FileUploadDTO> createBannners(List<MultipartFile> imageFiles, HttpServletRequest request) {
        List<FileUploadModel> fileUploadModels = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", imageFile.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullBannerPath(), generatedName, imageFile);

            FileUploadModel fileUploadModel = new FileUploadModel();
            fileUploadModel.setMimeType(imageFile.getContentType());
            fileUploadModel.setSize(imageFile.getSize());
            fileUploadModel.setOriginalName(imageFile.getOriginalFilename());
            fileUploadModel.setGeneratedName(generatedName);
            fileUploadModel.setBanner(true);
            fileUploadModels.add(fileUploadModel);
        }
        fileUploadModels = fileUploadRepository.saveAll(fileUploadModels);
        return fileUploadConverter.toBannerDTOs(fileUploadModels, request);
    }

    @Override
    public void deleteBannerModelAndFileById(Long id) {
        Optional<FileUploadModel> optional = fileUploadRepository.findById(id);
        if (optional.isPresent()) {
            FileUploadModel fileUploadModel = optional.get();
            fileStorageService.delete(applicationConfig.getFullBannerPath() + fileUploadModel.getGeneratedName());

            fileUploadRepository.delete(fileUploadModel);
        }
    }

    @Override
    public FileUploadModel findByBannerTypeAndIsBannerTrue(BannerType type) {
        return fileUploadRepository.findByBannerTypeAndIsBannerTrue(type);
    }

    @Override
    public String getFilePath(String path, String filename) {
        String filePath = null;
        if (filename != null) {
            String serverInfo = RequestUtils.getServerInfo(request);
            filePath = String.format("%s/data/%s%s", serverInfo, path, filename);
        }
        return filePath;
    }
}
