package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.dto.SubscriptionDetailDTO;
import vn.com.ids.myachef.dao.model.SubscriptionDetailModel;

@Component
public class SubscriptionDetailConverter {

    @Autowired
    private ModelMapper mapper;

    public SubscriptionDetailDTO toBasicDTO(SubscriptionDetailModel subscriptionDetailModel) {
        return mapper.map(subscriptionDetailModel, SubscriptionDetailDTO.class);
    }

    public List<SubscriptionDetailDTO> toBasicDTOs(List<SubscriptionDetailModel> subscriptionDetailModels) {
        List<SubscriptionDetailDTO> subscriptionDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionDetailModels)) {
            for (SubscriptionDetailModel subscriptionDetailModel : subscriptionDetailModels) {
                subscriptionDetailDTOs.add(toBasicDTO(subscriptionDetailModel));
            }
        }
        return subscriptionDetailDTOs;
    }
}
