package vn.com.ids.myachef.business.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.SaleDTO;
import vn.com.ids.myachef.dao.criteria.SaleCriteria;
import vn.com.ids.myachef.dao.model.ProductConfigModel;
import vn.com.ids.myachef.dao.model.SaleModel;

public interface SaleService extends IGenericService<SaleModel, Long> {

    Page<SaleModel> search(SaleCriteria flashSaleCriteria);

    SaleDTO create(SaleDTO flashSaleDTO, Long customerId);

    SaleDTO update(SaleModel saleModel, SaleDTO flashSaleDTO, Long customerId);

    List<SaleDTO> getAllFlashSaleActive(Long customerId);

    List<SaleDTO> findVoucherByConditon(List<String> nhanhVnProductIds, Long customerId, double amount);

    List<SaleDTO> findByCode(String code, List<String> nhanhVnProductIds, Long customerId, double amount);

    List<SaleModel> findIntroduceCustomerSaleCampaign();

    List<SaleModel> findGiftByCustomerId(Long customerId);

    SaleModel findIntroduceCustomerSaleCampaignByScopeIn(List<String> scopes);

    SaleDTO findById(Long id, Long customerId);

    boolean isExistByTimeFrame(LocalDateTime startDate, LocalDateTime endDate);

    boolean isExistByTimeFrameAndProductIds(LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds);

    List<Long> findProductIdsExistByTimeFrame(LocalDateTime fromDate, LocalDateTime toDate);

    void updateProductPrice();

    List<Long> findProductIdsExistByTimeFrameIgnoreBySaleId(LocalDateTime fromDate, LocalDateTime toDate, Long saleId);

    boolean existByTimeFrameAndScopeAll(LocalDateTime fromDate, LocalDateTime toDate);

    boolean existByTimeFrameAndScopeAllIgnoreSaleId(LocalDateTime fromDate, LocalDateTime toDate, Long saleId);

    boolean isExistByTimeFrameIgnoreId(LocalDateTime startDate, LocalDateTime endDate, Long id);

    boolean isExistByTimeFrameAndProductIdsIgnoreId(LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds, Long id);

    void updateProductPriceBySale(ProductConfigModel productConfigModel, Double newPrice);

    void revertProductPriceBySale(SaleModel saleModel);

    void updateProductPriceBySale(SaleModel saleModel);

    boolean existSaleForCustomerActiveInTimeFrame(LocalDateTime startDate, LocalDateTime endDate);

    boolean existSaleForCustomerActiveInTimeFrameIgnoreId(LocalDateTime startDate, LocalDateTime endDate, Long saleId);

    List<SaleModel> findSaleActiveScopeAllOrByProductId(Long productId);
}
