package vn.com.ids.myachef.business.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.SubscriptionCustomerDetailDTO;
import vn.com.ids.myachef.dao.criteria.SubscriptionCustomerDetailCriteria;
import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;

public interface SubscriptionCustomerDetailService extends IGenericService<SubscriptionCustomerDetailModel, Long> {

    boolean existsSubscriptionByCustomerId(Long customerId);

    SubscriptionCustomerDetailModel findByPaymentStatusNotAndCustomerId(PaymentStatus paid, Long customerId);

    Page<SubscriptionCustomerDetailModel> search(SubscriptionCustomerDetailCriteria criteria);

    SubscriptionCustomerDetailDTO uploadBill(Long subscriptionCustomerDetailId, String billUrl, HttpServletRequest request);

    SubscriptionCustomerDetailDTO updateStatus(Long subscriptionCustomerDetailId, SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO,
            HttpServletRequest request);

    List<SubscriptionCustomerDetailDTO> findYourGift(Long customerId, HttpServletRequest request);

    List<SubscriptionCustomerDetailModel> findGiftCanUse(Double totalAmount, Long customerId, List<Long> cartIds, String nhanhVnProductId, int quantity);

    boolean existPaidBySubscriptionId(Long subscriptionId);

    LocalDate findExpiredDateBySubscriptionIdAndCustomerId(Long subscriptionId, Long customerId);

}
