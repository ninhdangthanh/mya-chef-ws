package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.dto.SalePackageDTO;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.dao.model.SalePackageModel;

@Component
public class SalePackageConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductConfigService productConfigService;

    public List<SalePackageDTO> toBasicDTOs(List<SalePackageModel> models) {
        List<SalePackageDTO> salePackageDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (SalePackageModel salePackageModel : models) {
                salePackageDTOs.add(toBasicDTO(salePackageModel));
            }
        }
        return salePackageDTOs;
    }

    public SalePackageDTO toBasicDTO(SalePackageModel salePackageModel) {
        return mapper.map(salePackageModel, SalePackageDTO.class);
    }

    public SalePackageDTO toDTO(SalePackageModel salePackageModel, Long customerId) {
        SalePackageDTO salePackageDTO = toBasicDTO(salePackageModel);
        if (!CollectionUtils.isEmpty(salePackageModel.getProductIds())) {
            salePackageDTO.setProducts(productConfigService.mapProductByIds(salePackageModel.getProductIds(), customerId));
        }
        return salePackageDTO;
    }

    public SalePackageModel toModel(SalePackageDTO salePackageDTO) {
        return mapper.map(salePackageDTO, SalePackageModel.class);
    }

    public void update(SalePackageModel salePackageModel, SalePackageDTO salePackageDTO) {
        mapper.map(salePackageDTO, salePackageModel);
    }
}
