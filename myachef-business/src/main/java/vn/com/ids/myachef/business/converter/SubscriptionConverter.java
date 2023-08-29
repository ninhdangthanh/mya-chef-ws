package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.SubscriptionDTO;
import vn.com.ids.myachef.business.dto.SubscriptionDetailDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.business.service.ProductCategoryConfigService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SubscriptionCustomerDetailService;
import vn.com.ids.myachef.business.service.SubscriptionService;
import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;
import vn.com.ids.myachef.dao.model.ProductConfigModel;
import vn.com.ids.myachef.dao.model.SubscriptionDetailModel;
import vn.com.ids.myachef.dao.model.SubscriptionModel;

@Component
public class SubscriptionConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private ProductCategoryConfigService productCategoryConfigService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private SubscriptionCustomerDetailService subscriptionCustomerDetailService;

    public SubscriptionDTO toBasicDTO(SubscriptionModel model, HttpServletRequest request) {
        SubscriptionDTO subscriptionDTO = mapper.map(model, SubscriptionDTO.class);

        // hard code
        subscriptionDTO.setPurchaseUrl("https://addy.vn");

        if (StringUtils.hasText(model.getImage())) {
            subscriptionDTO.setImage(fileUploadService.getFilePath(applicationConfig.getSubscriptionPath(), model.getImage(), request));
        }
        return subscriptionDTO;
    }

    public List<SubscriptionDTO> toBasicDTOs(List<SubscriptionModel> models, HttpServletRequest request) {
        List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (SubscriptionModel subscriptionModel : models) {
                subscriptionDTOs.add(toBasicDTO(subscriptionModel, request));
            }
        }
        return subscriptionDTOs;
    }

    public SubscriptionDTO toDTO(SubscriptionModel subscriptionModel, Long customerId, HttpServletRequest request) {
        SubscriptionDTO subscriptionDTO = toBasicDTO(subscriptionModel, request);
        if (customerId != null) {
            subscriptionDTO
                    .setExpiredDate(subscriptionCustomerDetailService.findExpiredDateBySubscriptionIdAndCustomerId(subscriptionModel.getId(), customerId));
        }
        subscriptionDTO.setSubscriptionDetailDTOs(mapDetails(subscriptionModel.getSubscriptionDetails()));
        return subscriptionDTO;
    }

    private List<SubscriptionDetailDTO> mapDetails(List<SubscriptionDetailModel> subscriptionDetails) {
        List<SubscriptionDetailDTO> subscriptionDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionDetails)) {
            for (SubscriptionDetailModel subscriptionDetailModel : subscriptionDetails) {
                subscriptionDetailDTOs.add(mapper.map(subscriptionDetailModel, SubscriptionDetailDTO.class));
            }
        }
        return subscriptionDetailDTOs;
    }

    public SubscriptionModel toModel(SubscriptionDTO subscriptionDTO) {
        SubscriptionModel subscriptionModel = mapper.map(subscriptionDTO, SubscriptionModel.class);
        mapDetailModel(subscriptionModel, subscriptionDTO.getSubscriptionDetailDTOs());
        return subscriptionModel;
    }

    private List<SubscriptionDetailModel> mapDetailModel(SubscriptionModel subscriptionModel, List<SubscriptionDetailDTO> subscriptionDetailDTOs) {
        List<SubscriptionDetailModel> subscriptionDetailModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionDetailDTOs)) {
            for (SubscriptionDetailDTO subscriptionDetailDTO : subscriptionDetailDTOs) {
                String message = validate(subscriptionDetailDTO);
                if (StringUtils.hasText(message)) {
                    throw new BadRequestException(message);
                }

                SubscriptionDetailModel subscriptionDetailModel = mapper.map(subscriptionDetailDTO, SubscriptionDetailModel.class);
                subscriptionDetailModel.setId(null);
                subscriptionDetailModel.setSubscription(subscriptionModel);
                subscriptionModel.getSubscriptionDetails().add(subscriptionDetailModel);
                subscriptionDetailModels.add(subscriptionDetailModel);
            }
        }
        return subscriptionDetailModels;
    }

    private String validate(SubscriptionDetailDTO subscriptionDetailDTO) {
        String message = "";
        switch (subscriptionDetailDTO.getType()) {
        case COIN:
            if (subscriptionDetailDTO.getCoin() != null) {
                message += "Coin can not be null, ";
            }
            break;
        case LINK:
            if (!StringUtils.hasText(subscriptionDetailDTO.getLink())) {
                message += "Link can not be blank, ";
            }
            break;
        case PRODUCT:
            if (!StringUtils.hasText(subscriptionDetailDTO.getNhanhVnProductId())) {
                message += "nhanhVnProductId can not null with type = product, ";
            } else {
                ProductConfigModel productConfigModel = productConfigService.findByNhanhVnId(subscriptionDetailDTO.getNhanhVnProductId());
                if (productConfigModel == null) {
                    message += "Not found product with nhanhVnId = " + subscriptionDetailDTO.getNhanhVnProductId() + ", ";
                }
            }
            break;
        case GIFT_MONEY:
            if (!StringUtils.hasText(subscriptionDetailDTO.getNhanhVnProductCategoryId())) {
                message += "nhanhVnProductCategoryId can not null with type = gift money, ";
            } else {
                ProductCategoryConfigModel productCategoryConfigModel = productCategoryConfigService
                        .findByNhanhVnId(subscriptionDetailDTO.getNhanhVnProductCategoryId());
                if (productCategoryConfigModel == null) {
                    message += "Not found product category with nhanhVnId = " + subscriptionDetailDTO.getNhanhVnProductCategoryId() + ", ";
                }
            }

            if (subscriptionDetailDTO.getGiftMoney() == null) {
                message += "Gift money can not null with type = gift money, ";
            }
            break;
        case VOUCHER:
            if (subscriptionDetailDTO.getGiftMoney() == null) {
                message += "Gift money can not null with type = voucher, ";
            }
            break;
        case INTRODUCE_GIFT_MONEY:
            if (subscriptionDetailDTO.getGiftMoney() == null) {
                message += "Gift money can not null with type = introduce gift money, ";
            }

            if (CollectionUtils.isEmpty(subscriptionDetailDTO.getConditionSubscriptionIds())) {
                message += "ConditionSubscriptionIds can not blank with type = introduce gift money, ";
            } else {
                List<Long> conditionSubscriptionIds = new ArrayList<>(subscriptionDetailDTO.getConditionSubscriptionIds());
                if (conditionSubscriptionIds.contains(0l)) {
                    conditionSubscriptionIds.remove((long) 0);
                    if (!CollectionUtils.isEmpty(conditionSubscriptionIds)) {
                        List<SubscriptionModel> subscriptionModels = subscriptionService.findAllById(conditionSubscriptionIds);
                        if (CollectionUtils.isEmpty(subscriptionModels)) {
                            message += "Can not found subscriptions with ids = " + subscriptionDetailDTO.getConditionSubscriptionIds() + ", ";
                        } else if (subscriptionModels.size() != conditionSubscriptionIds.size()) {
                            List<Long> subscriptionModelIds = subscriptionModels.stream().map(SubscriptionModel::getId).collect(Collectors.toList());
                            message += "Can not found subscriptions with ids = " + conditionSubscriptionIds.removeAll(subscriptionModelIds) + ", ";
                        }
                    }
                } else {
                    List<SubscriptionModel> subscriptionModels = subscriptionService.findAllById(conditionSubscriptionIds);
                    if (CollectionUtils.isEmpty(subscriptionModels)) {
                        message += "Can not found subscriptions with ids = " + subscriptionDetailDTO.getConditionSubscriptionIds() + ", ";
                    } else if (subscriptionModels.size() != subscriptionDetailDTO.getConditionSubscriptionIds().size()) {
                        List<Long> subscriptionModelIds = subscriptionModels.stream().map(SubscriptionModel::getId).collect(Collectors.toList());
                        message += "Can not found subscriptions with ids = "
                                + subscriptionDetailDTO.getConditionSubscriptionIds().removeAll(subscriptionModelIds) + ", ";
                    }
                }
            }
            break;
        default:
            break;
        }
        return message;
    }

    public void update(SubscriptionModel subscriptionModel, SubscriptionDTO subscriptionDTO) {
        mapper.map(subscriptionDTO, subscriptionModel);
        if (subscriptionDTO.isUpdateDetails()) {
            subscriptionModel.getSubscriptionDetails().clear();
            mapDetailModel(subscriptionModel, subscriptionDTO.getSubscriptionDetailDTOs());
        }
    }

    public SubscriptionDTO toDTOForCustomer(SubscriptionModel subscriptionModel, HttpServletRequest request) {
        SubscriptionDTO subscriptionDTO = toBasicDTO(subscriptionModel, request);
        subscriptionDTO.setSubscriptionDetailDTOs(mapDetailsForCustomer(subscriptionModel.getSubscriptionDetails()));
        return subscriptionDTO;
    }

    private List<SubscriptionDetailDTO> mapDetailsForCustomer(List<SubscriptionDetailModel> subscriptionDetails) {
        List<SubscriptionDetailDTO> subscriptionDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionDetails)) {
            for (SubscriptionDetailModel subscriptionDetailModel : subscriptionDetails) {
                SubscriptionDetailDTO subscriptionDetailDTO = mapper.map(subscriptionDetailModel, SubscriptionDetailDTO.class);
                subscriptionDetailDTO.setLink(null);
                subscriptionDetailDTOs.add(subscriptionDetailDTO);
            }
        }
        return subscriptionDetailDTOs;
    }

    public List<SubscriptionDTO> toDTOs(List<SubscriptionModel> subscriptionModels, Long customerId, HttpServletRequest request) {
        List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionModels)) {
            for (SubscriptionModel subscriptionModel : subscriptionModels) {
                subscriptionDTOs.add(toDTO(subscriptionModel, customerId, request));
            }
        }
        return subscriptionDTOs;
    }
}
