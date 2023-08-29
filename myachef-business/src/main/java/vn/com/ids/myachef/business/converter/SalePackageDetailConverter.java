package vn.com.ids.myachef.business.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.ids.myachef.business.dto.SalePackageDetailDTO;
import vn.com.ids.myachef.dao.model.SalePackageDetailModel;

@Component
public class SalePackageDetailConverter {

    @Autowired
    private ModelMapper mapper;

    public SalePackageDetailModel toModel(SalePackageDetailDTO salePackageDetailDTO) {
        return mapper.map(salePackageDetailDTO, SalePackageDetailModel.class);
    }

    public SalePackageDetailDTO toBasicDTO(SalePackageDetailModel salePackageDetailModel) {
        return mapper.map(salePackageDetailModel, SalePackageDetailDTO.class);
    }

    public SalePackageDetailDTO toDTO(SalePackageDetailModel salePackageDetailModel) {
        SalePackageDetailDTO salePackageDetailDTO = toBasicDTO(salePackageDetailModel);
        if (salePackageDetailModel.getProduct() != null) {
            salePackageDetailDTO.setProductId(salePackageDetailModel.getProduct().getId());
        }
        salePackageDetailDTO.setSalePackageId(salePackageDetailModel.getSalePackage().getId());
        return salePackageDetailDTO;
    }

}
