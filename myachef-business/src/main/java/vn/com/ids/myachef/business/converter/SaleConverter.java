package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.dto.SaleDTO;
import vn.com.ids.myachef.business.service.SaleDetailService;
import vn.com.ids.myachef.dao.enums.PromotionType;
import vn.com.ids.myachef.dao.enums.SaleScope;
import vn.com.ids.myachef.dao.model.SaleModel;

@Component
public class SaleConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SaleDetailConverter saleDetailConverter;

    @Autowired
    private SaleDetailService saleDetailService;

    public List<SaleDTO> toBasicDTOs(List<SaleModel> models) {
        List<SaleDTO> saleDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (SaleModel saleModel : models) {
                saleDTOs.add(toBasicDTO(saleModel));
            }
        }
        return saleDTOs;
    }

    public SaleDTO toBasicDTO(SaleModel saleModel) {
        SaleDTO saleDTO = mapper.map(saleModel, SaleDTO.class);
        saleDTO.setTotalQuantityProduct(saleModel.getSaleDetails().size());
        return saleDTO;
    }

    public SaleModel toModel(SaleDTO saleDTO) {
        return mapper.map(saleDTO, SaleModel.class);
    }

    public SaleDTO toDTO(SaleModel saleModel, boolean isNewCustomer) {
        SaleDTO saleDTO = toBasicDTO(saleModel);
        if (!CollectionUtils.isEmpty(saleModel.getSaleDetails())) {
            saleDTO.setSaleDetailDTOs(saleDetailConverter.toDTOs(saleModel.getSaleDetails(), isNewCustomer));
        }
        return saleDTO;
    }

    public String validateVoucher(SaleDTO saleDTO) {
        StringBuilder builder = new StringBuilder();

        if (saleDTO.getMinimumOrderPrice() == null) {
            builder.append("field minimumOrderPrice can not be null with type = VOUCHER");
        }

        if (saleDTO.getTotalQuantity() == null) {
            builder.append("field totalQuantity can not be null with type = VOUCHER");
        }

        if (saleDTO.getMaxQuantityUseInUser() == null) {
            builder.append("field maxQuantityUseInUser can not be null with type = VOUCHER");
        }

        if (saleDTO.getTotalQuantity() != null && saleDTO.getMaxQuantityUseInUser() != null && saleDTO.getTotalQuantity() < saleDTO.getMaxQuantityUseInUser()) {
            builder.append("totalQuantity must be greater than the maxQuantityUseInUser");
        }

        if (saleDTO.getPromotionType() == null) {
            builder.append("field saleType can not be null with type = VOUCHER");
        } else {
            if (saleDTO.getPromotionType() == PromotionType.SALE && saleDTO.getDiscount() == null && saleDTO.getDiscountPercent() == null) {
                builder.append("field discount and discountPercent can not be null with type = VOUCHER and promotionType = SALE");
            } else if (saleDTO.getPromotionType() == PromotionType.RECEIVE_MONEY && saleDTO.getReceiveMoneyPercent() == null
                    && saleDTO.getReceiveMammyCoin() == null) {
                builder.append("field receiveMoneyPercent and receiveMammyCoin can not be null with type = VOUCHER and promotionType = SALE  ");
            }
        }
        return builder.toString();
    }

    public List<SaleDTO> toDTOs(List<SaleModel> saleModels, boolean isNewCustomer) {
        List<SaleDTO> saleDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saleModels)) {
            for (SaleModel saleModel : saleModels) {
                saleDTOs.add(toDTO(saleModel, isNewCustomer));
            }
        }
        return saleDTOs;
    }

    public void update(SaleModel saleModel, SaleDTO saleDTO) {
        mapper.map(saleDTO, saleModel);
    }

    public SaleDTO toDTOAndCheckCanUse(SaleModel saleModel, List<Long> saleIdByNhanhByNhanhVnProductIdIn, Long userId, double amount) {
        if (saleModel != null) {
            SaleDTO saleDTO = toBasicDTO(saleModel);
            if (saleModel.getSaleScope() == SaleScope.ALL) {
                saleDTO.setCanUse(true);
            } else if (saleModel.getSaleScope() == SaleScope.PRODUCT) {
                saleDTO.setCanUse(saleIdByNhanhByNhanhVnProductIdIn.contains(saleModel.getId()));
            }

            if (saleModel.getMinimumOrderPrice() != null && saleModel.getMinimumOrderPrice() > amount) {
                saleDTO.setCanUse(false);
            }

            if (Collections.frequency(saleModel.getUsedUserId(), userId) < saleModel.getMaxQuantityUseInUser()) {
                return saleDTO;
            }
        }
        return null;
    }

    public List<SaleDTO> toDTOsAndCheckCanUse(List<SaleModel> saleModels, Long customerId, List<String> nhanhVnProductIds, double amount) {
        List<SaleDTO> saleDTOs = new ArrayList<>();
        for (SaleModel saleModel : saleModels) {
            List<Long> saleIdByNhanhByNhanhVnProductIdIn = saleDetailService.findSaleIdByNhanhByNhanhVnProductIdIn(nhanhVnProductIds);
            saleDTOs.add(toDTOAndCheckCanUse(saleModel, saleIdByNhanhByNhanhVnProductIdIn, customerId, amount));
        }
        return saleDTOs;
    }
}
