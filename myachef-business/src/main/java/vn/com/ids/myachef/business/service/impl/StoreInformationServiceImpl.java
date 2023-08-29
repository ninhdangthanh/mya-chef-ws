package vn.com.ids.myachef.business.service.impl;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.converter.StoreInformationConverter;
import vn.com.ids.myachef.business.dto.StoreInformationDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.StoreInformationService;
import vn.com.ids.myachef.dao.criteria.StoreInformationCriteria;
import vn.com.ids.myachef.dao.criteria.builder.StoreInformationSpecificationBuilder;
import vn.com.ids.myachef.dao.model.StoreInformationModel;
import vn.com.ids.myachef.dao.repository.StoreInformationRepository;

@Service
@Transactional
public class StoreInformationServiceImpl extends AbstractService<StoreInformationModel, Long> implements StoreInformationService {

    @Autowired
    private StoreInformationConverter storeInformationConverter;

    private StoreInformationRepository storeInformationRepository;

    protected StoreInformationServiceImpl(StoreInformationRepository storeInformationRepository) {
        super(storeInformationRepository);
        this.storeInformationRepository = storeInformationRepository;
    }

    public Specification<StoreInformationModel> buildSpecification(StoreInformationCriteria storeInformationCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new StoreInformationSpecificationBuilder(root, criteriaBuilder) //
                .setDefault(storeInformationCriteria.getIsDefault())//
                .build();
    }

    @Override
    public Page<StoreInformationModel> findAll(StoreInformationCriteria storeInformationCriteria) {
        Specification<StoreInformationModel> specification = buildSpecification(storeInformationCriteria);
        Pageable pageable = buildPageable(storeInformationCriteria);
        return storeInformationRepository.findAll(specification, pageable);
    }

    @Override
    public StoreInformationModel findByIsDefaultTrue() {
        return storeInformationRepository.findByIsDefaultTrue();
    }

    @Override
    public StoreInformationDTO create(@Valid StoreInformationDTO storeInformationRequest) {
        if (Boolean.TRUE.equals(storeInformationRequest.getIsDefault())) {
            StoreInformationModel defautlStoreInformationModel = findByIsDefaultTrue();
            if (defautlStoreInformationModel != null) {
                defautlStoreInformationModel.setDefault(false);
                storeInformationRepository.save(defautlStoreInformationModel);
            }
        }

        StoreInformationModel storeInformationModel = storeInformationRepository.save(storeInformationConverter.toModel(storeInformationRequest));
        return storeInformationConverter.toDTO(storeInformationModel);
    }

    @Override
    public StoreInformationDTO update(StoreInformationDTO storeInformationRequest) {
        Long id = storeInformationRequest.getId();
        StoreInformationModel storeInformationModel = findOne(id);
        if (storeInformationModel == null) {
            throw new ResourceNotFoundException("Not found `StoreInfomation` by id: " + id);
        }

        boolean currentDefaultStatus = storeInformationModel.isDefault();
        if (!currentDefaultStatus) {
            StoreInformationModel defautlStoreInformationModel = findByIsDefaultTrue();
            if (defautlStoreInformationModel != null && currentDefaultStatus != defautlStoreInformationModel.isDefault()) {
                defautlStoreInformationModel.setDefault(false);
                storeInformationRepository.save(defautlStoreInformationModel);
            }
        }

        storeInformationConverter.mapDataToUpdate(storeInformationModel, storeInformationRequest);
        return storeInformationConverter.toDTO(storeInformationRepository.save(storeInformationModel));
    }

}
