package vn.com.ids.myachef.business.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.StoreInformationDTO;
import vn.com.ids.myachef.dao.criteria.StoreInformationCriteria;
import vn.com.ids.myachef.dao.model.StoreInformationModel;

public interface StoreInformationService extends IGenericService<StoreInformationModel, Long> {

    public Page<StoreInformationModel> findAll(StoreInformationCriteria storeInformationCriteria);

    public StoreInformationModel findByIsDefaultTrue();

    public StoreInformationDTO create(@Valid StoreInformationDTO storeInformationRequest);

    public StoreInformationDTO update(StoreInformationDTO storeInformationRequest);
}
