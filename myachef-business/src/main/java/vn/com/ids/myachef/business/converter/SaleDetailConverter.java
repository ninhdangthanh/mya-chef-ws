package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.dto.SaleDetailDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.dao.enums.SaleType;
import vn.com.ids.myachef.dao.model.SaleDetailModel;

@Component
public class SaleDetailConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductConfigConverter productConfigConverter;

    public SaleDetailModel toModel(SaleDetailDTO saleDetailDTO, SaleType saleType) {
        if (saleType == SaleType.FLASH_SALE || saleType == SaleType.SALE_CAMPAIGN || saleType == SaleType.SALE_FOR_NEW_CUSTOMER) {
            String message = validateSaleProductDetail(saleDetailDTO);

            if (StringUtils.hasText(message)) {
                throw new BadRequestException(message);
            }
        }
        return mapper.map(saleDetailDTO, SaleDetailModel.class);
    }

    private String validateSaleProductDetail(SaleDetailDTO saleDetailDTO) {
        StringBuilder builder = new StringBuilder();
        if (saleDetailDTO.getDiscount() == null && saleDetailDTO.getDiscountPercent() == null) {
            builder.append("field discount and discountPercent can not be null with type = FLASH_SALE, SALE_CAMPAIGN and saleScope = PRODUCT");
        }

        if (saleDetailDTO.getTotalQuantity() == null) {
            builder.append("field totalQuantity can not be null with type = FLASH_SALE, SALE_CAMPAIGN and saleScope = PRODUCT");
        }

        if (saleDetailDTO.getMaxQuantityUseInUser() == null) {
            builder.append("field maxQuantityUseInUser can not be null with type = FLASH_SALE, SALE_CAMPAIGN and saleScope = PRODUCT");
        }

        if (saleDetailDTO.getTotalQuantity() != null && saleDetailDTO.getMaxQuantityUseInUser() != null
                && saleDetailDTO.getTotalQuantity() < saleDetailDTO.getMaxQuantityUseInUser()) {
            builder.append("totalQuantity must be greater than the maxQuantityUseInUser");
        }
        return builder.toString();
    }

    public SaleDetailDTO toDTO(SaleDetailModel saleDetailModel, boolean isNewCustomer) {
        SaleDetailDTO saleDetailDTO = toBasicDTO(saleDetailModel);
        saleDetailDTO.setProductDTO(productConfigConverter.toBasicDTO(saleDetailModel.getProduct(), isNewCustomer));
        if (saleDetailDTO.getProductDTO() != null) {
            saleDetailDTO.setProductId(saleDetailDTO.getProductDTO().getId());
        }
        return saleDetailDTO;
    }

    public SaleDetailDTO toBasicDTO(SaleDetailModel saleDetailModel) {
        return mapper.map(saleDetailModel, SaleDetailDTO.class);
    }

    public List<SaleDetailDTO> toDTOs(List<SaleDetailModel> saleDetailModels, boolean isNewCustomer) {
        List<SaleDetailDTO> saleDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saleDetailModels)) {
            for (SaleDetailModel saleDetailModel : saleDetailModels) {
                saleDetailDTOs.add(toDTO(saleDetailModel, isNewCustomer));
            }
        }
        return saleDetailDTOs;
    }
}
