package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.ids.myachef.business.dto.CustomerAffiliateDetailDTO;
import vn.com.ids.myachef.dao.model.CustomerAffiliateDetailModel;

@Component
public class CustomerAffiliateDetailConverter {
    @Autowired
    private ModelMapper mapper;

    public CustomerAffiliateDetailDTO toBasicDTO(CustomerAffiliateDetailModel customerAffiliateDetailModel) {
        return mapper.map(customerAffiliateDetailModel, CustomerAffiliateDetailDTO.class);
    }

    public List<CustomerAffiliateDetailDTO> toBasicDTOs(List<CustomerAffiliateDetailModel> customerAffiliateDetailModels) {
        List<CustomerAffiliateDetailDTO> affiliateDetailDTOs = new ArrayList<>();
        for (CustomerAffiliateDetailModel customerAffiliateDetailModel : customerAffiliateDetailModels) {
            affiliateDetailDTOs.add(toBasicDTO(customerAffiliateDetailModel));
        }

        return affiliateDetailDTOs;
    }
}
