package vn.com.ids.myachef.business.converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.SubscriptionCustomerDetailDTO;
import vn.com.ids.myachef.business.dto.SubscriptionDetailDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;
import vn.com.ids.myachef.dao.model.SubscriptionModel;

@Component
public class SubscriptionCustomerDetailConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private SubscriptionConverter subscriptionConverter;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private FileUploadService fileUploadService;

    public SubscriptionCustomerDetailDTO toDTO(SubscriptionCustomerDetailModel subscriptionCustomerDetailModel, HttpServletRequest request) {
        SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO = toBasicDTO(subscriptionCustomerDetailModel, request);
        if (subscriptionCustomerDetailModel != null) {
            mapCustomer(subscriptionCustomerDetailDTO, subscriptionCustomerDetailModel.getCustomer());
            mapSubscription(subscriptionCustomerDetailDTO, subscriptionCustomerDetailModel.getSubscription(), request);
        }
        return subscriptionCustomerDetailDTO;
    }

    private void mapSubscription(SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO, SubscriptionModel subscription, HttpServletRequest request) {
        if (subscription != null) {
            subscriptionCustomerDetailDTO.setSubscriptionDTO(subscriptionConverter.toBasicDTO(subscription, request));
        }
    }

    private void mapCustomer(SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO, CustomerModel customer) {
        if (customer != null) {
            subscriptionCustomerDetailDTO.setCustomerDTO(customerConverter.toBasicDTO(customer));
        }
    }

    public List<SubscriptionCustomerDetailDTO> toBasicDTOs(List<SubscriptionCustomerDetailModel> models, HttpServletRequest request) {
        List<SubscriptionCustomerDetailDTO> subscriptionCustomerDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (SubscriptionCustomerDetailModel subscriptionCustomerDetailModel : models) {
                subscriptionCustomerDetailDTOs.add(toBasicDTO(subscriptionCustomerDetailModel, request));
            }
        }
        return subscriptionCustomerDetailDTOs;
    }

    public SubscriptionCustomerDetailDTO toBasicDTO(SubscriptionCustomerDetailModel model, HttpServletRequest request) {
        SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO = null;
        if (model != null) {
            subscriptionCustomerDetailDTO = mapper.map(model, SubscriptionCustomerDetailDTO.class);
            if (StringUtils.hasText(model.getBillImage())) {
                subscriptionCustomerDetailDTO
                        .setBillImage(fileUploadService.getFilePath(applicationConfig.getSubscriptionBillPath(), model.getBillImage(), request));
            }
        }
        return subscriptionCustomerDetailDTO;
    }

    public boolean update(SubscriptionCustomerDetailModel subscriptionCustomerDetailModel, SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO) {
        boolean isBuySuccess = false;
        if (subscriptionCustomerDetailDTO.getPaymentStatus() != null) {
            if (subscriptionCustomerDetailModel.getPaymentStatus() == PaymentStatus.PENDING
                    && subscriptionCustomerDetailDTO.getPaymentStatus() == PaymentStatus.PAID) {
                subscriptionCustomerDetailModel.setExpiredDate(LocalDate.now().plusMonths(subscriptionCustomerDetailModel.getSubscription().getMonth()));
                isBuySuccess = true;
            }
            subscriptionCustomerDetailModel.setPaymentStatus(subscriptionCustomerDetailDTO.getPaymentStatus());
        }

        return isBuySuccess;
    }

    public List<SubscriptionCustomerDetailDTO> toGiftDTOs(List<SubscriptionCustomerDetailModel> subscriptionCustomerDetailModels, HttpServletRequest request) {
        List<SubscriptionCustomerDetailDTO> subscriptionCustomerDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionCustomerDetailModels)) {
            for (SubscriptionCustomerDetailModel subscriptionCustomerDetailModel : subscriptionCustomerDetailModels) {
                subscriptionCustomerDetailDTOs.add(toGiftDTO(subscriptionCustomerDetailModel, request));
            }
        }
        return subscriptionCustomerDetailDTOs;
    }

    public SubscriptionCustomerDetailDTO toGiftDTO(SubscriptionCustomerDetailModel subscriptionCustomerDetailModel, HttpServletRequest request) {
        SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO = toBasicDTO(subscriptionCustomerDetailModel, request);
        if (subscriptionCustomerDetailModel.getSubscriptionDetail() != null) {
            subscriptionCustomerDetailDTO
                    .setSubscriptionDetailDTO(mapper.map(subscriptionCustomerDetailModel.getSubscriptionDetail(), SubscriptionDetailDTO.class));
        }
        subscriptionCustomerDetailDTO.setCanUseGift(subscriptionCustomerDetailModel.getGiftQuantity() > subscriptionCustomerDetailModel.getGiftTotalUsed());
        return subscriptionCustomerDetailDTO;
    }

    public List<SubscriptionCustomerDetailDTO> toDTOs(List<SubscriptionCustomerDetailModel> models, HttpServletRequest request) {
        List<SubscriptionCustomerDetailDTO> subscriptionCustomerDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (SubscriptionCustomerDetailModel subscriptionCustomerDetailModel : models) {
                subscriptionCustomerDetailDTOs.add(toDTO(subscriptionCustomerDetailModel, request));
            }
        }
        return subscriptionCustomerDetailDTOs;
    }

    public List<SubscriptionCustomerDetailDTO> toDTOAndAddSubscriptionDetails(List<SubscriptionCustomerDetailModel> models, HttpServletRequest request) {
        List<SubscriptionCustomerDetailDTO> subscriptionCustomerDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (SubscriptionCustomerDetailModel subscriptionCustomerDetailModel : models) {
                SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO = toBasicDTO(subscriptionCustomerDetailModel, request);
                subscriptionCustomerDetailDTO
                        .setSubscriptionDetailDTO(mapper.map(subscriptionCustomerDetailModel.getSubscriptionDetail(), SubscriptionDetailDTO.class));
                subscriptionCustomerDetailDTO.setCanUseGift(true);
                subscriptionCustomerDetailDTOs.add(subscriptionCustomerDetailDTO);
            }
        }
        return subscriptionCustomerDetailDTOs;
    }
}
