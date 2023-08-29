package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.FileUploadConverter;
import vn.com.ids.myachef.business.converter.SalePackageConverter;
import vn.com.ids.myachef.business.dto.FileUploadDTO;
import vn.com.ids.myachef.business.dto.SalePackageDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.business.service.SalePackageService;
import vn.com.ids.myachef.dao.criteria.SalePackageCriteria;
import vn.com.ids.myachef.dao.criteria.builder.SalePackageSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.FileUploadModel;
import vn.com.ids.myachef.dao.model.SalePackageModel;
import vn.com.ids.myachef.dao.repository.SalePackageRepository;

@Transactional
@Service
@Slf4j
public class SalePackageServiceImpl extends AbstractService<SalePackageModel, Long> implements SalePackageService {

    private SalePackageRepository salePackageRepository;

    protected SalePackageServiceImpl(SalePackageRepository salePackageRepository) {
        super(salePackageRepository);
        this.salePackageRepository = salePackageRepository;
    }

    @Autowired
    private SalePackageConverter salePackageConverter;

    @Autowired
    private FileUploadConverter fileUploadConverter;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;

    private Specification<SalePackageModel> buildSpecification(SalePackageCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new SalePackageSpecificationBuilder(root, criteriaBuilder) //
                .setName(criteria.getName()) //
                .setStatus(criteria.getStatus()) //
                .build(); //
    }

    @Override
    public Page<SalePackageModel> search(SalePackageCriteria salePackageCriteria) {
        Pageable pageable = buildPageable(salePackageCriteria);
        Specification<SalePackageModel> specification = buildSpecification(salePackageCriteria);
        return salePackageRepository.findAll(specification, pageable);
    }

    @Override
    public SalePackageDTO create(SalePackageDTO salePackageDTO, Long customerId) {
        if (!CollectionUtils.isEmpty(salePackageDTO.getProductIds()) && salePackageDTO.getProductIds().size() > salePackageDTO.getTotalProduct()) {
            throw new BadRequestException("Product size can not greater than total product");
        }
        SalePackageModel salePackageModel = salePackageConverter.toModel(salePackageDTO);
        salePackageModel.setStatus(Status.ACTIVE);
        if (salePackageDTO.getPeriodTime() == null) {
            salePackageModel.setPeriodTime(1);
        }

        salePackageRepository.save(salePackageModel);
        log.info("------------------ Create - END ----------------");
        return salePackageConverter.toDTO(salePackageModel, customerId);
    }

    @Override
    public SalePackageDTO update(SalePackageModel salePackageModel, SalePackageDTO salePackageDTO, Long customerId) {
        if (!CollectionUtils.isEmpty(salePackageDTO.getProductIds()) && salePackageDTO.getProductIds().size() > salePackageDTO.getTotalProduct()) {
            throw new BadRequestException("Product size can not greater than total product");
        }
        salePackageConverter.update(salePackageModel, salePackageDTO);
        if (salePackageModel.getPeriodTime() == null) {
            salePackageModel.setPeriodTime(1);
        }

        salePackageRepository.save(salePackageModel);
        log.info("------------------ Update - END ----------------");
        return salePackageConverter.toDTO(salePackageModel, customerId);
    }

    @Override
    public void updateStatusInActiveByIds(List<Long> ids) {
        salePackageRepository.updateStatusInActiveByIds(ids);
    }

    @Override
    public List<SalePackageDTO> getActiveSalePackage() {
        List<SalePackageDTO> salePackageDTOs = new ArrayList<>();
        List<SalePackageModel> salePackageModels = salePackageRepository.findByStatus(Status.ACTIVE);
        if (!CollectionUtils.isEmpty(salePackageModels)) {
            salePackageDTOs = salePackageConverter.toBasicDTOs(salePackageModels);
        }
        return salePackageDTOs;
    }

    @Override
    public FileUploadDTO getBanner(Long id, HttpServletRequest request) {
        FileUploadDTO fileUploadDTO = null;
        FileUploadModel fileUploadModel = fileUploadService.findOne(id);
        if (fileUploadModel != null) {
            fileUploadDTO = fileUploadConverter.toDTO(fileUploadModel, applicationConfig.getBannerPath(), request);
        }
        return fileUploadDTO;
    }
}
