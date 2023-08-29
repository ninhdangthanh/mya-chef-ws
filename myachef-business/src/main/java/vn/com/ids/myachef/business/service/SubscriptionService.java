package vn.com.ids.myachef.business.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.SubscriptionCustomerDetailDTO;
import vn.com.ids.myachef.business.dto.SubscriptionDTO;
import vn.com.ids.myachef.dao.criteria.SubscriptionCriteria;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.SubscriptionModel;

public interface SubscriptionService extends IGenericService<SubscriptionModel, Long> {

    Page<SubscriptionModel> search(SubscriptionCriteria criteria);

    SubscriptionDTO create(SubscriptionDTO subscriptionDTO, MultipartFile image, HttpServletRequest request);

    SubscriptionDTO update(SubscriptionModel subscriptionModel, SubscriptionDTO subscriptionDTO, MultipartFile image, HttpServletRequest request);

    void deleteAll(List<SubscriptionModel> subscriptionModels);

    SubscriptionCustomerDetailDTO buy(Long subscriptionId, Long customerId, SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO,
            HttpServletRequest request);

    List<SubscriptionDTO> findPaid(Long customerId, HttpServletRequest request);

    List<SubscriptionDTO> findYourSubscription(Long customerId, HttpServletRequest request);

    List<SubscriptionModel> findByStatus(Status status);
}
