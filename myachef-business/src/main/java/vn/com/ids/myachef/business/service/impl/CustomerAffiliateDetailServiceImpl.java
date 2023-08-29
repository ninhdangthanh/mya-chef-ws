package vn.com.ids.myachef.business.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.CustomerAffiliateDetailConverter;
import vn.com.ids.myachef.business.converter.CustomerConverter;
import vn.com.ids.myachef.business.dto.CustomerAffiliateDetailDTO;
import vn.com.ids.myachef.business.dto.CustomerDTO;
import vn.com.ids.myachef.business.service.CustomerAffiliateDetailService;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.dao.criteria.CustomerAffiliateDetailCriteria;
import vn.com.ids.myachef.dao.criteria.builder.CustomerAffiliateDetailBuilder;
import vn.com.ids.myachef.dao.model.CustomerAffiliateDetailModel;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.repository.CustomerAffiliateDetailRepository;

@Service
@Transactional
@Slf4j
public class CustomerAffiliateDetailServiceImpl extends AbstractService<CustomerAffiliateDetailModel, Long> implements CustomerAffiliateDetailService {

    private CustomerAffiliateDetailRepository customerAffiliateDetailRepository;

    @Autowired
    private CustomerAffiliateDetailConverter customerAffiliateDetailConverter;

    protected CustomerAffiliateDetailServiceImpl(CustomerAffiliateDetailRepository customerAffiliateDetailRepository) {
        super(customerAffiliateDetailRepository);
        this.customerAffiliateDetailRepository = customerAffiliateDetailRepository;
    }

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerConverter customerConverter;

    @Override
    public boolean existsByAffiliateCustomerIdAndReferredCustomerId(Long affiliateCustomerId, Long referredCustomerId) {
        return customerAffiliateDetailRepository.existsByAffiliateCustomerIdAndReferredCustomerId(affiliateCustomerId, referredCustomerId);
    }

    public Specification<CustomerAffiliateDetailModel> buildSpecification(CustomerAffiliateDetailCriteria customerAffiliateDetailCriteria) {
        return (root, criteriaQuery, criteriaBuilder) -> new CustomerAffiliateDetailBuilder(root, criteriaBuilder)
                .setAffiliateCustomerId(customerAffiliateDetailCriteria.getAffiliateCustomerId())
                .setReferredCustomerId(customerAffiliateDetailCriteria.getReferredCustomerId()).build();
    }

    @Override
    public Page<CustomerAffiliateDetailDTO> search(CustomerAffiliateDetailCriteria customerAffiliateDetailCriteria) {
        Pageable pageable = buildPageable(customerAffiliateDetailCriteria);
        Specification<CustomerAffiliateDetailModel> specification = buildSpecification(customerAffiliateDetailCriteria);
        Page<CustomerAffiliateDetailModel> page = customerAffiliateDetailRepository.findAll(specification, pageable);
        List<CustomerAffiliateDetailDTO> customerAffiliateDetailDTOs = customerAffiliateDetailConverter.toBasicDTOs(page.getContent());
        log.info("------------------ Search customer affiliate criteria - START ----------------");
        return new PageImpl<>(customerAffiliateDetailDTOs, pageable, page.getTotalElements());
    }

    @Override
    public boolean existsByAffiliateCustomerId(Long id) {
        return customerAffiliateDetailRepository.existsByAffiliateCustomerId(id);
    }

    @Override
    public Page<CustomerDTO> findReferredCustomerByPeriodTime(LocalDate fromDate, LocalDate toDate, int pageIndex, int pageSize) {
        int start = 0;
        if (pageIndex > 0) {
            start = (pageIndex * pageSize);
        }
        Map<Long, Long> countByReferredCustomerId = new HashedMap<>();

        List<Object[]> results = customerAffiliateDetailRepository.countByReferredCustomerId(fromDate, toDate, start, pageSize);
        if (results != null) {
            for (Object[] result : results) {
                countByReferredCustomerId.putIfAbsent(Long.parseLong(String.valueOf(result[0])), Long.parseLong(String.valueOf(result[1])));
            }
        }

        List<CustomerDTO> customerDTOs = new ArrayList<>();

        Long totalElements = 0l;

        if (!CollectionUtils.isEmpty(countByReferredCustomerId)) {
            totalElements = customerAffiliateDetailRepository.countByReferred(fromDate, toDate);
            List<CustomerModel> customerModels = customerService.findAllById(new ArrayList<>(countByReferredCustomerId.keySet()));
            if (!CollectionUtils.isEmpty(customerModels)) {
                Map<Long, CustomerModel> mapById = customerModels.stream().collect(Collectors.toMap(CustomerModel::getId, x -> x));
                countByReferredCustomerId.forEach((customerId, count) -> {
                    CustomerModel customerModel = mapById.get(customerId);
                    if (customerModel != null) {
                        CustomerDTO customerDTO = customerConverter.toBasicDTO(customerModel);
                        customerDTO.setTotalCustomerInPutReferralCode(count);
                        customerDTOs.add(customerDTO);
                    }
                });
            }
        }
        Pageable pageable = buildPageable(pageIndex, pageSize);
        log.info("------------------ Get referred customer by time - END ----------------");
        return new PageImpl<>(customerDTOs, pageable, totalElements);
    }

    @Override
    public List<CustomerAffiliateDetailModel> findByReferredCustomerId(Long referredCustomerId) {
        return customerAffiliateDetailRepository.findByReferredCustomerId(referredCustomerId);
    }

    @Override
    public List<CustomerAffiliateDetailModel> findByAffiliateCustomerId(Long affiliateCustomerId) {
        return customerAffiliateDetailRepository.findByAffiliateCustomerId(affiliateCustomerId);
    }
}
