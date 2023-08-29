package vn.com.ids.myachef.business.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.ids.myachef.business.dto.SalePackageCustomerDTO;
import vn.com.ids.myachef.dao.model.SalePackageModel;

@Component
public class SalePackageCustomerConverter {

    @Autowired
    private ModelMapper mapper;

    public SalePackageCustomerDTO toBasicDTO(SalePackageModel salePackageModel) {
        return mapper.map(salePackageModel, SalePackageCustomerDTO.class);
    }

}
