package vn.com.ids.myachef.business.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.OrderDTO;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.UpdateOrderEventData;
import vn.com.ids.myachef.business.payload.reponse.order.CalculateResponse;
import vn.com.ids.myachef.business.payload.request.SubscriptionGiftRequest;
import vn.com.ids.myachef.dao.criteria.OrderCriteria;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.model.ProductConfigModel;
import vn.com.ids.myachef.dao.model.SystemConfigModel;
import vn.com.ids.myachef.dao.repository.extended.model.CartProduct;

public interface OrderService extends IGenericService<OrderModel, Long> {

    public Page<OrderModel> findAll(OrderCriteria orderCriteria);

    public Page<OrderDTO> findByCriteriaForUser(OrderCriteria orderCriteria, HttpServletRequest request);

    public OrderDTO findById(Long id, HttpServletRequest request);

    public Page<OrderDTO> findByCriteriaForAdmin(OrderCriteria orderCriteria, HttpServletRequest request);

    public OrderDTO create(@Valid OrderDTO orderDTO, Long authenticatedUserId, HttpServletRequest request);

    public boolean updateStatusByNhanhVnId(String nhanhVnId, OrderStatus canceled);

    public String cancel(String nhanhVnId);

    public OrderModel findByNhanhVnId(String nhanhVnId);

    public void updateStatusByNhanhWebhooks(UpdateOrderEventData data);

    public String reorder(String nhanhVnId, Long authenticatedUserId);

    // public String uploadPaymentTransferImage(Long id, MultipartFile image, HttpServletRequest request);

    public CalculateResponse calculate(List<CartProduct> cartProducts, ProductConfigModel product, int quantity, Long authenticatedUserId,
            List<Long> voucherIds, SystemConfigModel systemConfig, List<SubscriptionGiftRequest> subscriptionGifts, List<Long> cartIds, String nhanhVnProductId,
            Long coin);

    public boolean existsByCustomerId(Long customerId);

    public void revertAllWhenCancelOrder(OrderModel orderModel);

    Integer countByCustomerIdAndStatusIn(Long customerId, List<OrderStatus> status);

    public String uploadBillImage(Long id, String image, String link);
}
