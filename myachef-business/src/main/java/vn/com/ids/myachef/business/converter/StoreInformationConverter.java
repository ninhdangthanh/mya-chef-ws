package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.dto.StoreInformationDTO;
import vn.com.ids.myachef.dao.model.StoreInformationModel;

@Component
public class StoreInformationConverter {

    @Autowired
    private ModelMapper mapper;

    public StoreInformationDTO toBasicDTO(StoreInformationModel storeInformationModel) {
        StoreInformationDTO informationDTO =  mapper.map(storeInformationModel, StoreInformationDTO.class);
        informationDTO.setIsDefault(storeInformationModel.isDefault());
        return informationDTO;
    }

    public List<StoreInformationDTO> toBasicDTOs(List<StoreInformationModel> storeInformationModels) {
        if (CollectionUtils.isEmpty(storeInformationModels)) {
            return new ArrayList<>();
        }

        List<StoreInformationDTO> storeInformationDTOs = new ArrayList<>();
        for (StoreInformationModel storeInformationModel : storeInformationModels) {
            storeInformationDTOs.add(toBasicDTO(storeInformationModel));
        }

        return storeInformationDTOs;
    }

    public StoreInformationDTO toDTO(StoreInformationModel storeInformationModel) {
        return toBasicDTO(storeInformationModel);
    }

    public List<StoreInformationDTO> toDTOs(List<StoreInformationModel> storeInformationModels) {
        if (CollectionUtils.isEmpty(storeInformationModels)) {
            return new ArrayList<>();
        }

        List<StoreInformationDTO> storeInformationDTOs = new ArrayList<>();
        for (StoreInformationModel storeInformationModel : storeInformationModels) {
            storeInformationDTOs.add(toDTO(storeInformationModel));
        }

        return storeInformationDTOs;
    }

    public StoreInformationModel toModel(StoreInformationDTO storeInformationDTO) {
        StoreInformationModel storeInformationModel = mapper.map(storeInformationDTO, StoreInformationModel.class);
        if (Boolean.TRUE.equals(storeInformationDTO.getIsDefault())) {
            storeInformationModel.setDefault(storeInformationDTO.getIsDefault());
        }
        return storeInformationModel;
    }

    public void mapDataToUpdate(StoreInformationModel storeInformationModel, StoreInformationDTO storeInformationRequest) {
        if (Boolean.TRUE.equals(storeInformationRequest.getIsDefault())) {
            storeInformationModel.setDefault(storeInformationRequest.getIsDefault());
        }

        if (storeInformationRequest.getIntroduction() != null) {
            storeInformationModel.setIntroduction(storeInformationRequest.getIntroduction());
        }

        if (storeInformationRequest.getIntroduction() != null) {
            storeInformationModel.setIntroduction(storeInformationRequest.getIntroduction());
        }

        if (storeInformationRequest.getPaymentMethod() != null) {
            storeInformationModel.setPaymentMethod(storeInformationRequest.getPaymentMethod());
        }

        if (storeInformationRequest.getPrivacyPolicy() != null) {
            storeInformationModel.setPrivacyPolicy(storeInformationRequest.getPrivacyPolicy());
        }

        if (storeInformationRequest.getReturnPolicy() != null) {
            storeInformationModel.setReturnPolicy(storeInformationRequest.getReturnPolicy());
        }

        if (storeInformationRequest.getDeliveryPolicy() != null) {
            storeInformationModel.setDeliveryPolicy(storeInformationRequest.getDeliveryPolicy());
        }

        if (storeInformationRequest.getInspectionPolicy() != null) {
            storeInformationModel.setInspectionPolicy(storeInformationRequest.getInspectionPolicy());
        }

        if (storeInformationRequest.getResponsibility() != null) {
            storeInformationModel.setResponsibility(storeInformationRequest.getResponsibility());
        }

        if (storeInformationRequest.getDisclaimer() != null) {
            storeInformationModel.setDisclaimer(storeInformationRequest.getDisclaimer());
        }

    }

}
