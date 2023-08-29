package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.CustomerConverter;
import vn.com.ids.myachef.business.converter.CustomerOfSalePackageConverter;
import vn.com.ids.myachef.business.converter.ProductConfigConverter;
import vn.com.ids.myachef.business.converter.SalePackageCustomerConverter;
import vn.com.ids.myachef.business.converter.SalePackageDetailConverter;
import vn.com.ids.myachef.business.dto.CustomerOfSalePackageDTO;
import vn.com.ids.myachef.business.dto.SalePackageCustomerDTO;
import vn.com.ids.myachef.business.dto.SalePackageDetailDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SalePackageDetailService;
import vn.com.ids.myachef.business.service.SalePackageService;
import vn.com.ids.myachef.dao.enums.SalePackageDetailType;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.SalePackageDetailModel;
import vn.com.ids.myachef.dao.model.SalePackageModel;
import vn.com.ids.myachef.dao.repository.SalePackageDetailRepository;

@Transactional
@Service
@Slf4j
public class SalePackageDetailServiceImpl extends AbstractService<SalePackageDetailModel, Long> implements SalePackageDetailService {

    @Autowired
    SalePackageDetailRepository salePackageDetailRepository;

    @Autowired
    SalePackageDetailConverter salePackageDetailConverter;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SalePackageService salePackageService;

    @Autowired
    private SalePackageCustomerConverter salePackageCustomerConverter;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private ProductConfigConverter productConfigConverter;

    @Autowired
    private CustomerOfSalePackageConverter customerOfSalePackageConverter;

    protected SalePackageDetailServiceImpl(SalePackageDetailRepository salePackageDetailRepository) {
        super(salePackageDetailRepository);
        this.salePackageDetailRepository = salePackageDetailRepository;
    }

    @Override
    public SalePackageDetailDTO create(SalePackageDetailDTO salePackageDetailDTO) {
        SalePackageModel salePackageModel = salePackageService.findOne(salePackageDetailDTO.getSalePackageId());
        CustomerModel customer = customerService.findOne(salePackageDetailDTO.getCustomerId());

        if (customer == null) {
            throw new ResourceNotFoundException("Can not found Customer with id: " + salePackageDetailDTO.getCustomerId());
        }
        if (salePackageModel == null) {
            throw new ResourceNotFoundException("Can not found Sale Package with id: " + salePackageDetailDTO.getSalePackageId());
        }

        SalePackageDetailModel salePackageDetailModel = salePackageDetailConverter.toModel(salePackageDetailDTO);

        salePackageDetailModel.setType(SalePackageDetailType.BUY);

        salePackageDetailModel.setSalePackage(salePackageModel);
        salePackageModel.getSalePackages().add(salePackageDetailModel);

        save(salePackageDetailModel);

        return salePackageDetailConverter.toDTO(salePackageDetailModel);
    }

    public List<SalePackageCustomerDTO> showByCustomer(Long id) {
        CustomerModel customer = customerService.findOne(id);

        if (customer == null) {
            throw new ResourceNotFoundException("Can not found Customer with id: " + id);
        }

        List<SalePackageDetailModel> salePackageDetails = salePackageDetailRepository.findByCustomerIdAndType(id, SalePackageDetailType.BUY);

        List<Long> salePackageIds = salePackageDetails.stream().map(obj -> obj.getSalePackage().getId()).collect(Collectors.toList());

        List<SalePackageModel> salePackages = salePackageIds.stream().map(item -> salePackageService.findOne(item)).collect(Collectors.toList());

        List<SalePackageCustomerDTO> salePackageCustomerDTOs = new ArrayList<>();

        // for (SalePackageModel salePackage : salePackages) {
        // SalePackageCustomerDTO salePackageCustomerDTO = salePackageCustomerConverter.toBasicDTO(salePackage);
        // List<ProductConfigDTO> receiveProducts = salePackage.getSalePackages().stream().filter(item -> item.getType() ==
        // SalePackageDetailType.USE)
        // .map(m -> m.getProduct()).map(p -> productConfigConverter.toBasicDTO(p)).collect(Collectors.toList());
        // salePackageCustomerDTO.setReceivedProducts(receiveProducts);
        //
        // List<ProductConfigDTO> productOfSalePackages = salePackage.getProductIds().stream().map(i ->
        // productConfigService.findOne(i))
        // .map(p -> productConfigConverter.toBasicDTO(p)).collect(Collectors.toList());
        //
        // List<ProductConfigDTO> notReceivedProducts = new ArrayList<>(productOfSalePackages);
        //
        // notReceivedProducts.removeAll(receiveProducts);
        //
        // salePackageCustomerDTO.setNotReceivedProducts(notReceivedProducts);
        //
        // salePackageCustomerDTOs.add(salePackageCustomerDTO);
        // }

        log.info("------------------ ShowForCustomer - END ----------------");

        return salePackageCustomerDTOs;
    }

    @Override
    public CustomerOfSalePackageDTO showCustomerBoughtPackage(Long salePackageId) {
        SalePackageModel salePackage = salePackageService.findOne(salePackageId);

        if (salePackage == null) {
            throw new ResourceNotFoundException("Can not get sale package with id: " + salePackageId);
        }

        List<CustomerModel> customers = salePackage.getSalePackages().stream().filter(i -> i.getType() == SalePackageDetailType.BUY).map(s -> s.getCustomerId())
                .map(t -> customerService.findOne(t)).collect(Collectors.toList());

        CustomerOfSalePackageDTO customerOfSalePackage = customerOfSalePackageConverter.toBasicDTO(salePackage);
        customerOfSalePackage.setCustomers(customerConverter.toDTOs(customers));

        log.info("------------------ ShowCustomerBoughtSalePackage - END ----------------");
        return customerOfSalePackage;
    }
}
