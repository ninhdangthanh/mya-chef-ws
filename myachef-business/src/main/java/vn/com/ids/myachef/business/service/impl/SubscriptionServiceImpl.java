package vn.com.ids.myachef.business.service.impl;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.SubscriptionConverter;
import vn.com.ids.myachef.business.converter.SubscriptionCustomerDetailConverter;
import vn.com.ids.myachef.business.dto.SubscriptionCustomerDetailDTO;
import vn.com.ids.myachef.business.dto.SubscriptionDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.SubscriptionCustomerDetailService;
import vn.com.ids.myachef.business.service.SubscriptionService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.criteria.SubscriptionCriteria;
import vn.com.ids.myachef.dao.criteria.builder.SubscriptionSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.enums.SubscriptionCustomerType;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;
import vn.com.ids.myachef.dao.model.SubscriptionModel;
import vn.com.ids.myachef.dao.repository.SubscriptionRepository;

@Transactional
@Service
@Slf4j
public class SubscriptionServiceImpl extends AbstractService<SubscriptionModel, Long> implements SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    protected SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        super(subscriptionRepository);
        this.subscriptionRepository = subscriptionRepository;
    }

    @Autowired
    private SubscriptionConverter subscriptionConverter;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SubscriptionCustomerDetailConverter subscriptionCustomerDetailConverter;

    @Autowired
    private SubscriptionCustomerDetailService subscriptionCustomerDetailService;

    private Specification<SubscriptionModel> buildSpecification(SubscriptionCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new SubscriptionSpecificationBuilder(root, criteriaBuilder) //
                .setName(criteria.getName()) //
                .setTagLine(criteria.getTagLine()) //
                .build(); //
    }

    @Override
    public Page<SubscriptionModel> search(SubscriptionCriteria criteria) {
        Pageable pageable = buildPageable(criteria);
        Specification<SubscriptionModel> specification = buildSpecification(criteria);
        return subscriptionRepository.findAll(specification, pageable);
    }

    @Override
    public SubscriptionDTO create(SubscriptionDTO subscriptionDTO, MultipartFile image, HttpServletRequest request) {
        SubscriptionModel subscriptionModel = subscriptionConverter.toModel(subscriptionDTO);
        subscriptionModel.setStatus(Status.ACTIVE);
        if (image != null) {
            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullSubscriptionPath(), generatedName, image);
            subscriptionModel.setImage(generatedName);
        }
        subscriptionRepository.save(subscriptionModel);
        log.info("------------------ Create - END ----------------");
        return subscriptionConverter.toDTO(subscriptionModel, null, request);
    }

    @Override
    public SubscriptionDTO update(SubscriptionModel subscriptionModel, SubscriptionDTO subscriptionDTO, MultipartFile image, HttpServletRequest request) {
        String oldImage = subscriptionModel.getImage();
        subscriptionDTO.setUpdateDetails(!subscriptionCustomerDetailService.existPaidBySubscriptionId(subscriptionModel.getId()));
        subscriptionConverter.update(subscriptionModel, subscriptionDTO);

        if (image != null) {
            if (StringUtils.hasText(oldImage)) {
                fileStorageService.delete(applicationConfig.getFullSubscriptionPath() + oldImage);
            }

            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullSubscriptionPath(), generatedName, image);
            subscriptionModel.setImage(generatedName);
        } else {
            subscriptionModel.setImage(oldImage);
        }

        subscriptionRepository.save(subscriptionModel);
        log.info("------------------ Update - END ----------------");
        SubscriptionDTO reponse = subscriptionConverter.toDTO(subscriptionModel, null, request);
        reponse.setUpdateDetails(subscriptionDTO.isUpdateDetails());
        return reponse;
    }

    @Override
    public void deleteAll(List<SubscriptionModel> subscriptionModels) {
        subscriptionRepository.deleteAll(subscriptionModels);
    }

    @Override
    public SubscriptionCustomerDetailDTO buy(Long subscriptionId, Long customerId, SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO,
            HttpServletRequest request) {
        SubscriptionModel subscription = findOne(subscriptionId);
        if (subscription == null) {
            throw new ResourceNotFoundException("Not found subscription with id: " + subscriptionId);
        }

        CustomerModel customer = customerService.findOne(customerId);
        if (customer == null) {
            throw new ResourceNotFoundException("Not found customer with id: " + customerId);
        }

        if (subscriptionCustomerDetailService.existsSubscriptionByCustomerId(customerId)) {
            throw new BadRequestException("Your subscription is early exixts");
        }

        SubscriptionCustomerDetailModel subscriptionCustomerDetailModel = subscriptionCustomerDetailService
                .findByPaymentStatusNotAndCustomerId(PaymentStatus.PAID, customerId);
        if (subscriptionCustomerDetailModel == null) {
            subscriptionCustomerDetailModel = new SubscriptionCustomerDetailModel();
            subscriptionCustomerDetailModel.setCustomer(customer);
            customer.getSubscriptionCustomerDetails().add(subscriptionCustomerDetailModel);
        }
        subscriptionCustomerDetailModel.setType(SubscriptionCustomerType.BUY);
        subscriptionCustomerDetailModel.setPaymentType(subscriptionCustomerDetailDTO.getPaymentType());
        subscriptionCustomerDetailModel.setCustomerFullName(subscriptionCustomerDetailDTO.getCustomerFullName());
        subscriptionCustomerDetailModel.setCustomerOrderPhoneNumber(subscriptionCustomerDetailDTO.getCustomerOrderPhoneNumber());
        subscriptionCustomerDetailModel.setPaymentStatus(PaymentStatus.PENDING);
        subscriptionCustomerDetailModel.setSubscription(subscription);
        subscription.getSubscriptionCustomerDetails().add(subscriptionCustomerDetailModel);

        subscriptionCustomerDetailService.save(subscriptionCustomerDetailModel);
        log.info("------------------ Buy subscription - END ----------------");
        return subscriptionCustomerDetailConverter.toDTO(subscriptionCustomerDetailModel, request);
    }

    @Override
    public List<SubscriptionDTO> findPaid(Long customerId, HttpServletRequest request) {
        List<SubscriptionModel> subscriptionModels = subscriptionRepository.findPaid(customerId);
        log.info("------------------ Find paid subscription - END ----------------");
        return subscriptionConverter.toBasicDTOs(subscriptionModels, request);
    }

    @Override
    public List<SubscriptionDTO> findYourSubscription(Long customerId, HttpServletRequest request) {
        List<SubscriptionModel> subscriptionModels = subscriptionRepository.findYourSubscription(customerId);
        log.info("------------------ Find your subscription - END ----------------");
        return subscriptionConverter.toDTOs(subscriptionModels, customerId, request);
    }

    @Override
    public List<SubscriptionModel> findByStatus(Status status) {
        return subscriptionRepository.findByStatus(status);
    }
}
