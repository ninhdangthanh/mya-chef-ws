package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.CustomerConverter;
import vn.com.ids.myachef.business.converter.OrderConverter;
import vn.com.ids.myachef.business.dto.CustomerDTO;
import vn.com.ids.myachef.business.dto.OrderDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.UpdateOrderEventData;
import vn.com.ids.myachef.business.payload.reponse.order.CalculateResponse;
import vn.com.ids.myachef.business.payload.request.SubscriptionGiftRequest;
import vn.com.ids.myachef.business.service.CartService;
import vn.com.ids.myachef.business.service.CustomerCoinHistoryService;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.OrderDetailService;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SaleDetailService;
import vn.com.ids.myachef.business.service.SaleService;
import vn.com.ids.myachef.business.service.SubscriptionCustomerDetailService;
import vn.com.ids.myachef.business.service.SystemConfigService;
import vn.com.ids.myachef.business.service.nhanhvn.NhanhVNOrderService;
import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVNOrderUpdateRequest;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNOrderCreateResponse;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNOrderUpdateResponse;
import vn.com.ids.myachef.dao.criteria.OrderCriteria;
import vn.com.ids.myachef.dao.criteria.builder.OrderSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryScope;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryType;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.OrderSubscriptionGiftType;
import vn.com.ids.myachef.dao.enums.OrderType;
import vn.com.ids.myachef.dao.enums.SaleScope;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.enums.SubscriptionDetailType;
import vn.com.ids.myachef.dao.model.CartModel;
import vn.com.ids.myachef.dao.model.CustomerCoinHistoryModel;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.model.OrderSubscriptionGiftModel;
import vn.com.ids.myachef.dao.model.ProductConfigModel;
import vn.com.ids.myachef.dao.model.SaleModel;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;
import vn.com.ids.myachef.dao.model.SubscriptionDetailModel;
import vn.com.ids.myachef.dao.model.SystemConfigModel;
import vn.com.ids.myachef.dao.repository.OrderRepository;
import vn.com.ids.myachef.dao.repository.extended.model.CartProduct;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl extends AbstractService<OrderModel, Long> implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private CartService cartService;

    @Autowired
    private NhanhVNOrderService nhanhVNOrderService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleDetailService saleDetailService;

    @Autowired
    private SubscriptionCustomerDetailService subscriptionCustomerDetailService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CustomerCoinHistoryService customerCoinHistoryService;

    protected OrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

    public Specification<OrderModel> buildSpecification(OrderCriteria orderCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new OrderSpecificationBuilder(root, criteriaBuilder) //
                .setTimeFrame(orderCriteria.getFrom(), orderCriteria.getTo()) //
                .setStatuses(orderCriteria.getStatuses()) //
                .setCustomerIds(orderCriteria.getCustomerIds()) //
                .setMoneyFrame(orderCriteria.getTotalMoneyFrom(), orderCriteria.getTotalMoneyTo())//
                .setPaymentMethod(orderCriteria.getPaymentMethod())//
                .setType(orderCriteria.getType())// )
                .build();
    }

    @Override
    public Page<OrderModel> findAll(OrderCriteria orderCriteria) {
        Specification<OrderModel> specification = buildSpecification(orderCriteria);
        Pageable pageable = buildPageable(orderCriteria);
        return orderRepository.findAll(specification, pageable);
    }

    @Override
    public Page<OrderDTO> findByCriteriaForUser(OrderCriteria orderCriteria, HttpServletRequest request) {
        return findByCriteria(orderCriteria, request);
    }

    public Page<OrderDTO> findByCriteria(OrderCriteria orderCriteria, HttpServletRequest request) {
        Page<OrderModel> page = findAll(orderCriteria);
        List<OrderDTO> orderDTOs = orderConverter.toDTOs(page.getContent(), request);
        Pageable pageable = PageRequest.of(orderCriteria.getPageIndex(), orderCriteria.getPageSize());
        return new PageImpl<>(orderDTOs, pageable, page.getTotalElements());
    }

    @Override
    public OrderDTO findById(Long id, HttpServletRequest request) {
        OrderModel orderModel = findOne(id);
        if (orderModel == null) {
            throw new ResourceNotFoundException(String.format("Not found order by id: %s", id));
        }
        OrderDTO orderDTO = orderConverter.toDTO(orderModel, request);
        orderDTO.setCustomerDTO(customerConverter.toBasicDTO(orderModel.getCustomer()));
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findByCriteriaForAdmin(OrderCriteria orderCriteria, HttpServletRequest request) {
        Page<OrderModel> page = findAll(orderCriteria);
        List<OrderModel> contents = page.getContent();

        List<Long> customerIds = contents.stream().map(order -> order.getCustomer().getId()).distinct().collect(Collectors.toList());
        List<CustomerModel> customerModels = customerService.findAllById(customerIds);
        Map<Long, CustomerModel> customerMap = customerModels.stream().collect(Collectors.toMap(CustomerModel::getId, Function.identity()));
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (OrderModel orderModel : contents) {
            if (customerMap.get(orderModel.getCustomer().getId()) != null) {
                OrderDTO orderDTO = orderConverter.toDTO(orderModel, request);
                orderDTO.setCustomerDTO(customerConverter.toBasicDTO(customerMap.get(orderModel.getCustomer().getId())));
                orderDTOs.add(orderDTO);
            }
        }
        Pageable pageable = PageRequest.of(orderCriteria.getPageIndex(), orderCriteria.getPageSize());
        return new PageImpl<>(orderDTOs, pageable, page.getTotalElements());
    }

    @Override
    public OrderDTO create(@Valid OrderDTO orderDTO, Long authenticatedUserId, HttpServletRequest request) {
        OrderModel orderModel = null;
        CalculateResponse calculateResponse = null;
        CustomerModel customerModel = customerService.findOne(authenticatedUserId);

        SystemConfigModel systemConfig = systemConfigService.findAll().stream().findFirst().orElseThrow(() -> {
            throw new ResourceNotFoundException("Not found SystemConfig");
        });

        if (!CollectionUtils.isEmpty(orderDTO.getCartIds())) {
            List<CartProduct> carts = cartService.findQuantityAndProductByCartIdIn(orderDTO.getCartIds());
            if (!CollectionUtils.isEmpty(carts)) {
                orderModel = buildNewOrder(orderDTO, customerModel);
                List<OrderDetailModel> orderDetails = new ArrayList<>();
                for (CartProduct cartProduct : carts) {
                    OrderDetailModel orderDetail = new OrderDetailModel();
                    orderDetail.setQuantity(cartProduct.getQuantity());
                    ProductConfigModel product = cartProduct.getProduct();
                    if (product.getStatus() != Status.ACTIVE) {
                        throw new BadRequestException("Product with id " + product.getId() + "is deleted");
                    }
                    orderDetail.addProductAndOrder(product, orderModel, product.getNhanhVnId());
                    orderDetails.add(orderDetail);
                }
                orderModel.setOrderDetails(orderDetails);

                calculateResponse = calculate(carts, null, 0, authenticatedUserId, orderDTO.getVoucherIds(), systemConfig, orderDTO.getSubscriptionGifts(),
                        orderDTO.getCartIds(), orderDTO.getNhanhVnProductId(), orderDTO.getCoin());

                cartService.deleteByIds(orderDTO.getCartIds());
            }
        } else if (orderDTO.getNhanhVnProductId() != null && orderDTO.getQuantity() > 0) {
            orderModel = buildNewOrder(orderDTO, customerModel);
            OrderDetailModel orderDetail = new OrderDetailModel();
            orderDetail.setQuantity(orderDTO.getQuantity());
            ProductConfigModel product = productConfigService.findByNhanhVnId(orderDTO.getNhanhVnProductId());
            if (product == null) {
                throw new ResourceNotFoundException(String.format("Not found product order by nhanhVN id: %s", orderDTO.getNhanhVnProductId()));
            }
            if (product.getStatus() != Status.ACTIVE) {
                throw new BadRequestException("Product with id " + product.getId() + "is deleted");
            }
            orderDetail.addProductAndOrder(product, orderModel, product.getNhanhVnId());
            orderModel.getOrderDetails().add(orderDetail);

            calculateResponse = calculate(null, product, orderDTO.getQuantity(), authenticatedUserId, orderDTO.getVoucherIds(), systemConfig,
                    orderDTO.getSubscriptionGifts(), orderDTO.getCartIds(), orderDTO.getNhanhVnProductId(), orderDTO.getCoin());
        } else {
            throw new BadRequestException("CartIds or nhanhProductId can not be null");
        }

        if (orderModel != null) {
            orderModel.setTotalAmount(calculateResponse.getTotalAmount());
            orderModel.setShipFee(calculateResponse.getShipFee());
            orderModel.setTotalMoneyDiscount(calculateResponse.getTotalMoneyDiscount());
            orderModel.setTotalMammnyCoinReceive(calculateResponse.getTotalMammyCoinReceive());
            orderModel.setShipDiscount(calculateResponse.getShipDiscount());
            orderModel.setTotalShipFee(calculateResponse.getTotalShipFee());
            orderModel.setTotalMoney(calculateResponse.getTotalPayment());
            orderModel.setTotalPayment(calculateResponse.getTotalPayment());
            orderModel.setCoinDiscount(calculateResponse.getCoinDiscount());
            if (!CollectionUtils.isEmpty(orderDTO.getVoucherIds())) {
                orderModel.setVoucherIds(orderDTO.getVoucherIds());

                List<SaleModel> saleModels = saleService.findAllById(orderDTO.getVoucherIds());
                if (!CollectionUtils.isEmpty(saleModels)) {
                    for (SaleModel saleModel : saleModels) {
                        saleModel.setTotalQuantityUsed(saleModel.getTotalQuantityUsed() + 1);
                        saleModel.getUsedUserId().add(authenticatedUserId);
                    }
                    saleService.saveAll(saleModels);
                }
            }

            orderModel.getOrderDetails().addAll(createSubscriptionGifts(orderModel, orderDTO.getSubscriptionGifts()));

            orderModel = orderRepository.save(orderModel);

            if (calculateResponse.getCoinDiscount() > 0) {
                long coinUsed = calculateResponse.getCoinDiscount() / applicationConfig.getCoinRatioConvert();
                customerModel.setCoin(customerModel.getCoin() - coinUsed);

                CustomerCoinHistoryModel coinHistory = new CustomerCoinHistoryModel();
                coinHistory.setCustomerFullName(customerModel.getFullName());
                coinHistory.setCustomerId(customerModel.getId());
                coinHistory.setQuantity(coinUsed);
                coinHistory.setScope(CustomerCoinHistoryScope.ORDER);
                coinHistory.setScopeId(orderModel.getId());
                coinHistory.setType(CustomerCoinHistoryType.USE);

                customerCoinHistoryService.save(coinHistory);

                customerService.save(customerModel);
            }

            if (calculateResponse.getTotalMammyCoinReceive() > 0) {
                customerCoinHistoryService.updateCoinForCustomer(customerModel, calculateResponse.getTotalMammyCoinReceive(), CustomerCoinHistoryScope.ORDER,
                        orderModel.getId(), CustomerCoinHistoryType.RECEIVE);
            }

            NhanhVNOrderCreateResponse nhanhVNOrderCreateResponse = nhanhVNOrderService.create(orderModel, calculateResponse);

            if (nhanhVNOrderCreateResponse != null && nhanhVNOrderCreateResponse.getData() != null) {
                orderModel.setNhanhVnId(String.valueOf(nhanhVNOrderCreateResponse.getData().getOrderId()));
                orderModel = orderRepository.save(orderModel);
                orderModel.setSyncToNhanhVn(true);
            } else {
                log.error("Can not create order nhanh error: "
                        + nhanhVNOrderCreateResponse.getMessages().toString().substring(1, nhanhVNOrderCreateResponse.getMessages().toString().length() - 1));
                orderModel.setSyncToNhanhVn(false);
            }
        }

        log.info("------------------ OrderController - Create - END ----------------");
        return orderConverter.toDTO(orderModel, request);
    }

    private List<OrderDetailModel> createSubscriptionGifts(OrderModel orderModel, List<SubscriptionGiftRequest> subscriptionGifts) {
        List<OrderDetailModel> orderDetailModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionGifts)) {
            Map<Long, SubscriptionGiftRequest> mapSubscriptionGiftByGiftId = subscriptionGifts.stream()
                    .collect(Collectors.toMap(SubscriptionGiftRequest::getGiftId, x -> x));

            List<SubscriptionCustomerDetailModel> gifts = subscriptionCustomerDetailService.findAllById(new ArrayList<>(mapSubscriptionGiftByGiftId.keySet()));
            if (!CollectionUtils.isEmpty(gifts)) {
                for (SubscriptionCustomerDetailModel subscriptionCustomerDetailModel : gifts) {
                    SubscriptionDetailModel subscriptionDetailModel = subscriptionCustomerDetailModel.getSubscriptionDetail();
                    SubscriptionGiftRequest subscriptionGiftRequest = mapSubscriptionGiftByGiftId.get(subscriptionCustomerDetailModel.getId());
                    ProductConfigModel productConfigModel = null;
                    if (subscriptionDetailModel.getType() == SubscriptionDetailType.PRODUCT
                            || subscriptionDetailModel.getType() == SubscriptionDetailType.GIFT_MONEY) {

                        if (subscriptionDetailModel.getType() == SubscriptionDetailType.PRODUCT) {
                            subscriptionGiftRequest.setNhanhVnProductId(subscriptionDetailModel.getNhanhVnProductId());
                        }

                        productConfigModel = productConfigService.findByNhanhVnId(subscriptionGiftRequest.getNhanhVnProductId());
                        OrderDetailModel orderDetailModel = createSubscriptionGiftForOrder(orderModel, productConfigModel, subscriptionDetailModel,
                                subscriptionGiftRequest.getQuantity(), subscriptionCustomerDetailModel.getId());
                        if (orderDetailModel != null) {
                            orderDetailModels.add(orderDetailModel);
                        }
                    } else {
                        OrderSubscriptionGiftModel orderSubscriptionGiftModel = new OrderSubscriptionGiftModel();
                        orderSubscriptionGiftModel.setType(OrderSubscriptionGiftType.SUCCESS);
                        orderSubscriptionGiftModel.setOrder(orderModel);
                        orderSubscriptionGiftModel.setSubscriptionCustomerDetailId(subscriptionDetailModel.getId());
                        orderModel.getSubscriptionGifts().add(orderSubscriptionGiftModel);
                        orderSubscriptionGiftModel.setQuantity(subscriptionDetailModel.getQuantity());
                    }

                    updateGiftQuantity(subscriptionDetailModel, subscriptionGiftRequest, subscriptionCustomerDetailModel, productConfigModel);
                }
            }
        }

        orderDetailService.saveAll(orderDetailModels);
        return orderDetailModels;
    }

    private void updateGiftQuantity(SubscriptionDetailModel subscriptionDetailModel, SubscriptionGiftRequest subscriptionGiftRequest,
            SubscriptionCustomerDetailModel subscriptionCustomerDetailModel, ProductConfigModel productConfigModel) {
        if (subscriptionDetailModel.getType() == SubscriptionDetailType.GIFT_MONEY) {
            if (productConfigModel != null) {
                subscriptionCustomerDetailModel.setGiftTotalUsed(subscriptionCustomerDetailModel.getGiftTotalUsed()
                        + (productConfigModel.getPrice().intValue() * subscriptionGiftRequest.getQuantity()));
            }
        } else {
            subscriptionCustomerDetailModel.setGiftTotalUsed(subscriptionCustomerDetailModel.getGiftTotalUsed() + subscriptionGiftRequest.getQuantity());
        }
    }

    private OrderDetailModel createSubscriptionGiftForOrder(OrderModel orderModel, ProductConfigModel productConfigModel,
            SubscriptionDetailModel subscriptionDetailModel, Integer quantity, Long subscriptionCustomerDetailId) {
        if (productConfigModel != null) {
            OrderDetailModel orderDetailModel = new OrderDetailModel();
            orderDetailModel.setProduct(productConfigModel);
            orderDetailModel.setOrder(orderModel);
            orderDetailModel.setNhanhVnProductId(productConfigModel.getNhanhVnId());
            orderDetailModel.setProductCurrentPrice(0.0);
            orderDetailModel.setTotalProductPrice(0.0);
            orderDetailModel.setSubscriptionGift(true);

            OrderSubscriptionGiftModel orderSubscriptionGiftModel = new OrderSubscriptionGiftModel();
            orderSubscriptionGiftModel.setType(OrderSubscriptionGiftType.SUCCESS);
            orderSubscriptionGiftModel.setOrder(orderModel);
            orderSubscriptionGiftModel.setSubscriptionCustomerDetailId(subscriptionCustomerDetailId);
            if (subscriptionDetailModel.getType() == SubscriptionDetailType.PRODUCT) {
                orderSubscriptionGiftModel.setQuantity(quantity);
            } else if (subscriptionDetailModel.getType() == SubscriptionDetailType.GIFT_MONEY) {
                orderSubscriptionGiftModel.setQuantity((int) (productConfigModel.getPrice() * quantity));
            }
            orderModel.getSubscriptionGifts().add(orderSubscriptionGiftModel);

            return orderDetailModel;
        }

        return null;
    }

    private OrderModel buildNewOrder(OrderDTO orderDTO, CustomerModel customerModel) {
        OrderModel orderModel = new OrderModel();
        orderModel.setStatus(OrderStatus.NEW);
        orderModel.setType(OrderType.SHIPPING);
        orderModel.setPaymentMethod(orderDTO.getPaymentMethod());
        orderModel.setNote(orderDTO.getNote());

        CustomerDTO customerDTO = orderDTO.getCustomerDTO();
        orderModel.setRecipientName(customerDTO.getFullName());
        orderModel.setRecipientPhoneNumber(customerDTO.getPhoneNumber());
        orderModel.setRecipientChildName(customerDTO.getChildName());
        orderModel.setRecipientChildGender(customerDTO.getChildGender());
        orderModel.setRecipientCity(customerDTO.getCity());
        orderModel.setRecipientDistrict(customerDTO.getDistrict());
        orderModel.setRecipientWard(customerDTO.getWard());
        orderModel.setRecipientAddress(customerDTO.getAddress());
        orderModel.setRecipientCityId(customerDTO.getCityId());
        orderModel.setRecipientDistrictId(customerDTO.getDistrictId());
        orderModel.setRecipientWardId(customerDTO.getWardId());

        orderModel.addCustomer(customerModel);
        return orderModel;
    }

    @Override
    public boolean updateStatusByNhanhVnId(String nhanhVnId, OrderStatus canceled) {
        return orderRepository.updateStatusByNhanhVnId(nhanhVnId, canceled) > 0;
    }

    @Override
    public String cancel(String nhanhVnId) {
        OrderModel orderModel = findByNhanhVnId(nhanhVnId);
        if (orderModel == null) {
            throw new ResourceNotFoundException("Not found order with nhanhVnId: " + nhanhVnId);
        }

        if (OrderStatus.NEW != orderModel.getStatus() && //
                OrderStatus.CONFIRMING != orderModel.getStatus() && //
                OrderStatus.CUSTOMER_CONFIRMING != orderModel.getStatus() && //
                OrderStatus.CONFIRMED != orderModel.getStatus()) {

            throw new BadRequestException("Cannot cancel order, status is " + orderModel.getStatus());
        }
        orderModel.setStatus(OrderStatus.CANCELED);

        NhanhVNOrderUpdateResponse nhanhVNOrderUpdateResponse = nhanhVNOrderService.update(buildNhanhVNOrderUpdateStatusRequest(orderModel));
        if (nhanhVNOrderUpdateResponse.getData() != null && nhanhVNOrderUpdateResponse.getMessages().isEmpty()) {
            revertAllWhenCancelOrder(orderModel);

            orderRepository.save(orderModel);
        } else {
            throw new BadRequestException(
                    nhanhVNOrderUpdateResponse.getMessages().toString().substring(1, nhanhVNOrderUpdateResponse.getMessages().toString().length() - 1));
        }

        return "Order cancellation successful";
    }

    private NhanhVNOrderUpdateRequest buildNhanhVNOrderUpdateStatusRequest(OrderModel orderModel) {
        NhanhVNOrderUpdateRequest nhanhVNOrderUpdateRequest = new NhanhVNOrderUpdateRequest();
        nhanhVNOrderUpdateRequest.setOrderId(orderModel.getNhanhVnId());
        nhanhVNOrderUpdateRequest.setStatus(orderModel.getStatus().getValue());
        return nhanhVNOrderUpdateRequest;
    }

    @Override
    public OrderModel findByNhanhVnId(String nhanhVnId) {
        return orderRepository.findByNhanhVnId(nhanhVnId);
    }

    @Override
    public void updateStatusByNhanhWebhooks(UpdateOrderEventData data) {
        if (data != null) {
            OrderModel orderModel = findByNhanhVnId(String.valueOf(data.getOrderId()));
            if (orderModel != null) {
                OrderStatus currentStatus = orderModel.getStatus();
                if (data.getStatus() != null && !currentStatus.getValue().equals(data.getStatus())) {
                    OrderStatus orderStatus = OrderStatus.getByValue(data.getStatus());
                    orderModel.setStatus(orderStatus);
                    save(orderModel);

                    if (OrderStatus.SUCCESS.getValue().equalsIgnoreCase(data.getStatus())) {
                        customerService.createGitfForReferredCustomer(orderModel.getCustomer().getId());
                    }

                    revert(orderModel, data.getStatus());
                }
            }
        }
    }

    private void revert(OrderModel orderModel, String status) {
        if (orderModel.getStatus() != OrderStatus.CANCELED && orderModel.getStatus() != OrderStatus.ABORTED
                && orderModel.getStatus() != OrderStatus.CARRIER_CANCELED) {
            if (OrderStatus.CANCELED.getValue().equalsIgnoreCase(status) || OrderStatus.ABORTED.getValue().equalsIgnoreCase(status)
                    || OrderStatus.CARRIER_CANCELED.getValue().equalsIgnoreCase(status)) {
                revertAllWhenCancelOrder(orderModel);
            }
        }
    }

    @Override
    public String reorder(String nhanhVnId, Long authencatedUserId) {
        OrderModel orderModel = findByNhanhVnId(nhanhVnId);
        if (orderModel == null) {
            throw new ResourceNotFoundException("Not found order by nhanhId " + nhanhVnId);
        }

        List<String> nhanhProductIds = orderModel.getOrderDetails().stream().map(OrderDetailModel::getNhanhVnProductId).collect(Collectors.toList());
        List<CartModel> currentCartItems = cartService.findByUserId(authencatedUserId);

        List<CartModel> updateCartsQuantity = new ArrayList<>();
        if (!CollectionUtils.isEmpty(currentCartItems)) {
            Map<String, CartModel> mapCurrentCartMap = currentCartItems.stream().collect(Collectors.toMap(CartModel::getNhanhVnProductId, Function.identity()));
            for (int i = nhanhProductIds.size() - 1; i >= 0; i--) {
                String nhanhProductId = nhanhProductIds.get(i);
                if (mapCurrentCartMap.get(nhanhProductId) != null) {
                    CartModel updateCartQuantity = mapCurrentCartMap.get(nhanhProductId);
                    currentCartItems.remove(updateCartQuantity);
                    updateCartQuantity.setQuantity(updateCartQuantity.getQuantity() + 1);
                    updateCartQuantity.setPreSelect(true);

                    updateCartsQuantity.add(updateCartQuantity);
                    nhanhProductIds.remove(nhanhProductId);
                }
            }
        }

        if (!updateCartsQuantity.isEmpty()) {
            cartService.saveAll(updateCartsQuantity);
        }
        List<ProductConfigModel> newProductConfigModels = productConfigService.findByNhanhVnIdIn(nhanhProductIds);
        if (!newProductConfigModels.isEmpty()) {
            List<CartModel> newCartItems = newProductConfigModels.stream()//
                    .filter(product -> product.getStatus() == Status.ACTIVE)//
                    .map(product -> {
                        CartModel cartModel = new CartModel();
                        cartModel.setNhanhVnProductId(product.getNhanhVnId());
                        cartModel.setUserId(authencatedUserId);
                        cartModel.setQuantity(1);
                        cartModel.setPreSelect(true);
                        return cartModel;
                    }).collect(Collectors.toList());
            cartService.saveAll(newCartItems);
        }

        return "Success";
    }

    @Override
    public CalculateResponse calculate(List<CartProduct> cartProducts, //
            ProductConfigModel product, int quantity, //
            Long authenticatedUserId, //
            List<Long> voucherIds, //
            SystemConfigModel systemConfig, List<SubscriptionGiftRequest> subscriptionGifts, List<Long> cartIds, String nhanhVnProductId, Long coin) {
        CalculateResponse calculateResponse = new CalculateResponse();
        List<Long> productIds = new ArrayList<>();

        boolean isNewCustomer = false;
        if (authenticatedUserId != null) {
            isNewCustomer = customerService.isNewCustomer(authenticatedUserId);
        }

        if (!CollectionUtils.isEmpty(cartProducts)) {
            productIds = cartProducts.stream().map(cart -> cart.getProduct().getId()).collect(Collectors.toList());
            calculateResponse = calculateMoney(cartProducts, null, 0, systemConfig, isNewCustomer, authenticatedUserId);
        } else if (product != null) {
            productIds.add(product.getId());
            calculateResponse = calculateMoney(null, product, quantity, systemConfig, isNewCustomer, authenticatedUserId);
        }

        calculateVoucher(calculateResponse, voucherIds, productIds, authenticatedUserId);

        calculateSubscriptionGift(calculateResponse, cartIds, nhanhVnProductId, quantity, subscriptionGifts, authenticatedUserId);

        if (coin != null) {
            long coinMoney = coin * applicationConfig.getCoinRatioConvert();
            if (coinMoney <= calculateResponse.getTotalPayment()) {
                calculateResponse.setCoinDiscount(coinMoney);
                calculateResponse.setTotalPayment(calculateResponse.getTotalPayment() - coinMoney);
            } else {
                calculateResponse.setCoinDiscount((long) calculateResponse.getTotalPayment());
                calculateResponse.setTotalPayment(0);
            }
        }

        return calculateResponse;
    }

    private void calculateSubscriptionGift(CalculateResponse calculateResponse, List<Long> cartIds, String nhanhVnProductId, int quantity,
            List<SubscriptionGiftRequest> subscriptionGifts, Long authenticatedUserId) {
        if (!CollectionUtils.isEmpty(subscriptionGifts)) {
            Map<Long, Integer> mapSubscriptionGiftByGiftId = subscriptionGifts.stream()
                    .collect(Collectors.toMap(SubscriptionGiftRequest::getGiftId, SubscriptionGiftRequest::getQuantity));

            List<SubscriptionCustomerDetailModel> giftsCanUse = subscriptionCustomerDetailService.findGiftCanUse(calculateResponse.getTotalAmount(),
                    authenticatedUserId, cartIds, nhanhVnProductId, quantity);
            if (!CollectionUtils.isEmpty(giftsCanUse)) {
                List<Long> giftCanNotUse = new ArrayList<Long>(mapSubscriptionGiftByGiftId.keySet());
                giftCanNotUse.removeAll(giftsCanUse.stream().map(SubscriptionCustomerDetailModel::getId).collect(Collectors.toList()));
                if (!CollectionUtils.isEmpty(giftCanNotUse)) {
                    throw new BadRequestException("You can not use gifts with ids " + giftCanNotUse.toString());
                }

                updatePriceByGift(calculateResponse, mapSubscriptionGiftByGiftId);
            } else {
                throw new BadRequestException("You can not use this gift or this gift is expried");
            }
        }
    }

    private void updatePriceByGift(CalculateResponse calculateResponse, Map<Long, Integer> mapSubscriptionGiftByGiftId) {
        List<SubscriptionCustomerDetailModel> subscriptionCustomerDetailModels = subscriptionCustomerDetailService
                .findAllById(new ArrayList<Long>(mapSubscriptionGiftByGiftId.keySet()));
        if (!CollectionUtils.isEmpty(subscriptionCustomerDetailModels)) {
            for (SubscriptionCustomerDetailModel subscriptionCustomerDetailModel : subscriptionCustomerDetailModels) {
                Integer quantity = mapSubscriptionGiftByGiftId.get(subscriptionCustomerDetailModel.getId());
                if (quantity != null) {
                    if (subscriptionCustomerDetailModel.getLimit() != null && quantity > subscriptionCustomerDetailModel.getLimit()) {
                        throw new BadRequestException(
                                "Gift " + subscriptionCustomerDetailModel.getGiftDescription() + " is limit to " + subscriptionCustomerDetailModel.getLimit());
                    }
                    if (subscriptionCustomerDetailModel.getSubscriptionDetail().getType() == SubscriptionDetailType.VOUCHER) {
                        calculateResponse.setTotalMoneyDiscount(
                                calculateResponse.getTotalMoneyDiscount() + subscriptionCustomerDetailModel.getSubscriptionDetail().getGiftMoney() * quantity);
                        calculateResponse.setTotalPayment(
                                calculateResponse.getTotalPayment() - subscriptionCustomerDetailModel.getSubscriptionDetail().getGiftMoney() * quantity);
                    }
                }
            }
        }
    }

    private void calculateVoucher(CalculateResponse calculateResponse, List<Long> voucherIds, List<Long> productIds, Long userId) {
        if (!CollectionUtils.isEmpty(voucherIds)) {
            List<SaleModel> saleModels = saleService.findAllById(voucherIds);
            if (!CollectionUtils.isEmpty(saleModels)) {
                for (SaleModel saleModel : saleModels) {
                    calculateVoucherByScope(saleModel, calculateResponse, productIds, userId);
                }
            }
        }
    }

    private void calculateVoucherByScope(SaleModel saleModel, CalculateResponse calculateResponse, List<Long> productIds, Long userId) {
        validateVoucher(saleModel, productIds, userId);

        switch (saleModel.getPromotionType()) {
        case SALE:
            if (saleModel.getDiscount() != null && saleModel.getDiscount() > 0) {
                calculateResponse.setTotalMoneyDiscount(calculateResponse.getTotalMoneyDiscount() + saleModel.getDiscount());
                calculateResponse.setTotalPayment(calculateResponse.getTotalPayment() - saleModel.getDiscount());
            }
            if (saleModel.getDiscountPercent() != null && saleModel.getDiscountPercent() > 0) {
                if (saleModel.getMaxPromotion() != null && saleModel.getMaxPromotion() > 0) {
                    if ((calculateResponse.getTotalPayment() * saleModel.getMaxPromotion() / 100) > saleModel.getMaxPromotion()) {
                        calculateResponse.setTotalMoneyDiscount(calculateResponse.getTotalMoneyDiscount() + saleModel.getMaxPromotion());
                        calculateResponse.setTotalPayment(calculateResponse.getTotalPayment() - saleModel.getMaxPromotion());
                    } else {
                        calculateResponse.setTotalMoneyDiscount(
                                calculateResponse.getTotalMoneyDiscount() + (calculateResponse.getTotalPayment() * saleModel.getMaxPromotion() / 100));
                        calculateResponse.setTotalPayment(
                                calculateResponse.getTotalPayment() - (calculateResponse.getTotalPayment() * saleModel.getMaxPromotion() / 100));
                    }
                } else {
                    calculateResponse.setTotalMoneyDiscount(
                            calculateResponse.getTotalMoneyDiscount() + (calculateResponse.getTotalPayment() * saleModel.getMaxPromotion() / 100));
                    calculateResponse
                            .setTotalPayment(calculateResponse.getTotalPayment() - (calculateResponse.getTotalPayment() * saleModel.getMaxPromotion() / 100));
                }
            }
            break;
        case RECEIVE_MONEY:
            if (saleModel.getReceiveMammyCoin() != null && saleModel.getReceiveMammyCoin() > 0) {
                calculateResponse.setTotalMammyCoinReceive((long) (calculateResponse.getTotalMammyCoinReceive() + saleModel.getReceiveMammyCoin()));
            }
            if (saleModel.getReceiveMoneyPercent() != null && saleModel.getReceiveMoneyPercent() > 0) {
                if (saleModel.getMaxPromotion() != null && saleModel.getMaxPromotion() > 0) {
                    if (((calculateResponse.getTotalPayment() * saleModel.getMaxPromotion() / 100) / 1000) > saleModel.getMaxPromotion()) {
                        calculateResponse.setTotalMammyCoinReceive(calculateResponse.getTotalMammyCoinReceive() + saleModel.getMaxPromotion());
                    } else {
                        calculateResponse.setTotalMammyCoinReceive((long) (calculateResponse.getTotalMammyCoinReceive()
                                + ((calculateResponse.getTotalPayment() * saleModel.getMaxPromotion() / 100) / 1000)));
                    }
                } else {
                    calculateResponse.setTotalMammyCoinReceive((long) (calculateResponse.getTotalMammyCoinReceive()
                            + ((calculateResponse.getTotalPayment() * saleModel.getReceiveMoneyPercent() / 100) / 1000)));
                }
            }
            break;

        default:
            break;
        }
    }

    private void validateVoucher(SaleModel saleModel, List<Long> productIds, Long userId) {
        if (saleModel.getStartDate().isAfter(saleModel.getEndDate())) {
            throw new BadRequestException("This voucher is expired");
        }

        if (saleModel.getStatus() != Status.ACTIVE) {
            throw new BadRequestException("This voucher is not active");
        }

        if (saleModel.getTotalQuantityUsed() >= saleModel.getTotalQuantity()) {
            throw new BadRequestException("The amount of the voucher use has been exhausted");
        }

        if (Collections.frequency(saleModel.getUsedUserId(), userId) >= saleModel.getMaxQuantityUseInUser()) {
            throw new BadRequestException("You was used this voucher");
        }

        if (saleModel.getSaleScope() == SaleScope.PRODUCT) {
            List<Long> saleProductIds = saleDetailService.findProductIdBySaleId(saleModel.getId());
            saleProductIds.retainAll(productIds);
            if (CollectionUtils.isEmpty(saleProductIds)) {
                throw new BadRequestException("Can not use voucher with id = " + saleModel.getId());
            }
        }
    }

    private CalculateResponse calculateMoney(List<CartProduct> cartProducts, ProductConfigModel product, int quantity, SystemConfigModel systemConfig,
            boolean isNewCustomer, Long customerId) {
        CalculateResponse calculateResponse = new CalculateResponse();
        if (product != null) {
            calculateAmount(product, quantity, calculateResponse, isNewCustomer, customerId);
        } else if (!CollectionUtils.isEmpty(cartProducts)) {
            for (CartProduct cartProduct : cartProducts) {
                calculateAmount(cartProduct.getProduct(), cartProduct.getQuantity(), calculateResponse, isNewCustomer, customerId);
            }
        } else {
            throw new BadRequestException("product and cartProducts can not be null");
        }

        calculateShipFee(systemConfig, calculateResponse);

        // apply voucher, ... , > 399 free ship, ...
        calculateResponse.setTotalPayment(calculateResponse.getTotalAmount() + calculateResponse.getTotalShipFee());

        return calculateResponse;
    }

    private void calculateShipFee(SystemConfigModel systemConfig, CalculateResponse calculateResponse) {
        double shipFee = systemConfig.getShipPrice();
        if (calculateResponse.getTotalAmount() >= systemConfig.getMinimunFreeShipOrderTotalPrice()) {
            calculateResponse.setShipDiscount(shipFee);
            calculateResponse.setTotalShipFee(0);
        }
        calculateResponse.setShipFee(shipFee);
    }

    private void calculateAmount(ProductConfigModel product, int quantity, CalculateResponse calculateResponse, boolean isNewCustomer, Long customerId) {
        if (isNewCustomer && product.getSaleNewCustomerPrice() != -1) {
            if (quantity > product.getSaleNewCustomerQuantity()) {
                calculateResponse.setTotalAmount(calculateResponse.getTotalAmount() + product.getSaleNewCustomerPrice() * product.getSaleNewCustomerQuantity());
                if (product.getSalePriceBackup() != -1) {
                    calculateResponse.setTotalAmount(
                            calculateResponse.getTotalAmount() + product.getSalePriceBackup() * (quantity - product.getSaleNewCustomerQuantity()));
                } else {
                    calculateResponse
                            .setTotalAmount(calculateResponse.getTotalAmount() + product.getPrice() * (quantity - product.getSaleNewCustomerQuantity()));
                }
                calculateResponse.setPriceChangedNotification(true);
            } else {
                calculateResponse.setTotalAmount(calculateResponse.getTotalAmount() + product.getSaleNewCustomerPrice() * quantity);
            }
        } else {
            if (product.getSalePriceBackup() != -1) {
                List<SaleModel> saleModels = saleService.findSaleActiveScopeAllOrByProductId(product.getId());
                if (!CollectionUtils.isEmpty(saleModels)) {
                    Integer quantityProductSaleOrdered = orderRepository.findQuantityProductSaleOrdered(saleModels.get(0).getStartDate(),
                            saleModels.get(0).getEndDate(), customerId, product.getId());
                    if (quantityProductSaleOrdered != null) {
                        long quantityProductCanBuy = product.getSaleQuantity() - quantityProductSaleOrdered;
                        if (quantityProductCanBuy > 0) {
                            if (quantity > quantityProductCanBuy) {
                                calculateResponse.setTotalAmount(calculateResponse.getTotalAmount() + product.getPrice() * quantityProductCanBuy);
                                calculateResponse
                                        .setTotalAmount(calculateResponse.getTotalAmount() + product.getSalePriceBackup() * (quantity - quantityProductCanBuy));
                            } else {
                                calculateResponse.setTotalAmount(calculateResponse.getTotalAmount() + product.getPrice() * quantity);
                            }
                            calculateResponse.setPriceChangedNotification(true);
                        } else {
                            calculateResponse.setTotalAmount(calculateResponse.getTotalAmount() + product.getSalePriceBackup() * quantity);
                        }
                    } else {
                        if (quantity > product.getSaleQuantity()) {
                            calculateResponse.setTotalAmount(calculateResponse.getTotalAmount() + product.getPrice() * product.getSaleQuantity());
                            calculateResponse
                                    .setTotalAmount(calculateResponse.getTotalAmount() + product.getSalePriceBackup() * (quantity - product.getSaleQuantity()));
                            calculateResponse.setPriceChangedNotification(true);
                        } else {
                            calculateResponse.setTotalAmount(calculateResponse.getTotalAmount() + product.getPrice() * quantity);
                        }
                    }
                }
            } else {
                calculateResponse.setTotalAmount(calculateResponse.getTotalAmount() + product.getPrice() * quantity);
            }
        }
        if (product.getOldPrice() != null) {
            calculateResponse.setTotalOldProductPrice(calculateResponse.getTotalOldProductPrice() + product.getOldPrice());
        } else {
            if (product.getSalePriceBackup() != -1) {
                calculateResponse.setTotalOldProductPrice(calculateResponse.getTotalOldProductPrice() + product.getSalePriceBackup());
            } else {
                calculateResponse.setTotalOldProductPrice(calculateResponse.getTotalOldProductPrice() + product.getPrice());
            }
        }
    }

    // @Override
    // public String uploadPaymentTransferImage(Long id, MultipartFile image, HttpServletRequest request) {
    // OrderModel orderModel = findOne(id);
    // if (orderModel == null) {
    // throw new ResourceNotFoundException(String.format("Not found order by id: %s", id));
    // }
    //
    // String prefixName = UUID.randomUUID().toString();
    // String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
    // fileStorageService.upload(String.format(applicationConfig.getFullOrderPath(), orderModel.getId()), generatedName,
    // image);
    // orderModel.setPaymentImage(generatedName);
    // orderModel = save(orderModel);
    //
    // String imageLink = fileUploadService.getFilePath(applicationConfig.getOrderPath(), orderModel.getId(),
    // orderModel.getPaymentImage(), request);
    // NhanhVNOrderUpdateResponse nhanhVNOrderUpdateResponse = nhanhVNOrderService
    // .update(buildNhanhVNOrderUpdatePrivateDescriptionRequest(orderModel, imageLink));
    // if (nhanhVNOrderUpdateResponse.getData() != null && nhanhVNOrderUpdateResponse.getMessages().isEmpty()) {
    // orderRepository.save(orderModel);
    // } else {
    // throw new BadRequestException(
    // nhanhVNOrderUpdateResponse.getMessages().toString().substring(1,
    // nhanhVNOrderUpdateResponse.getMessages().toString().length() - 1));
    // }
    //
    // return imageLink;
    // }

    private NhanhVNOrderUpdateRequest buildNhanhVNOrderUpdatePrivateDescriptionRequest(OrderModel orderModel, String imageLink) {
        NhanhVNOrderUpdateRequest nhanhVNOrderUpdateRequest = new NhanhVNOrderUpdateRequest();
        nhanhVNOrderUpdateRequest.setOrderId(orderModel.getNhanhVnId());
        nhanhVNOrderUpdateRequest
                .setPrivateDescription("Link hình ảnh chuyển khoản VUI LÒNG KIỂM TRA SỐ TIỀN ĐÃ NHẬN hình ảnh có thể bị làm giả \n" + imageLink);
        return nhanhVNOrderUpdateRequest;
    }

    @Override
    public Integer countByCustomerIdAndStatusIn(Long customerId, List<OrderStatus> status) {
        return orderRepository.countByCustomerIdAndStatusIn(customerId, status);
    }

    @Override
    public boolean existsByCustomerId(Long customerId) {
        return orderRepository.existsByCustomerId(customerId);
    }

    @Override
    public void revertAllWhenCancelOrder(OrderModel orderModel) {
        // revert voucher
        if (!CollectionUtils.isEmpty(orderModel.getVoucherIds())) {
            revertVoucher(orderModel.getVoucherIds());
        }

        // revert subscription gift
        if (!CollectionUtils.isEmpty(orderModel.getSubscriptionGifts())) {
            revertSubscriptionGift(orderModel.getSubscriptionGifts());
        }

        // revert coin
        if (orderModel.getCoinDiscount() > 0) {
            customerCoinHistoryService.updateCoinForCustomer(orderModel.getCustomer(), orderModel.getCoinDiscount(), CustomerCoinHistoryScope.ORDER,
                    orderModel.getId(), CustomerCoinHistoryType.REVERT);
        }

        if (orderModel.getTotalMammnyCoinReceive() > 0) {
            customerCoinHistoryService.updateCoinForCustomer(orderModel.getCustomer(), (long) -orderModel.getTotalMammnyCoinReceive(),
                    CustomerCoinHistoryScope.ORDER, orderModel.getId(), CustomerCoinHistoryType.REVERT);
        }

        save(orderModel);
    }

    private void revertSubscriptionGift(List<OrderSubscriptionGiftModel> subscriptionGifts) {
        Map<Long, OrderSubscriptionGiftModel> mapBySubscriptionCustomerDetailId = subscriptionGifts.stream()
                .collect(Collectors.toMap(OrderSubscriptionGiftModel::getSubscriptionCustomerDetailId, x -> x));

        List<SubscriptionCustomerDetailModel> gifts = subscriptionCustomerDetailService
                .findAllById(new ArrayList<>(mapBySubscriptionCustomerDetailId.keySet()));

        if (!CollectionUtils.isEmpty(gifts)) {
            for (SubscriptionCustomerDetailModel subscriptionCustomerDetailModel : gifts) {
                OrderSubscriptionGiftModel orderSubscriptionGiftModel = mapBySubscriptionCustomerDetailId.get(subscriptionCustomerDetailModel.getId());
                if (orderSubscriptionGiftModel != null) {
                    subscriptionCustomerDetailModel
                            .setGiftQuantity(subscriptionCustomerDetailModel.getGiftQuantity() + orderSubscriptionGiftModel.getQuantity());

                    subscriptionCustomerDetailModel
                            .setGiftTotalUsed(subscriptionCustomerDetailModel.getGiftTotalUsed() - orderSubscriptionGiftModel.getQuantity());

                    orderSubscriptionGiftModel.setType(OrderSubscriptionGiftType.REVERT);
                }
            }
        }

    }

    private void revertVoucher(List<Long> voucherIds) {
        List<SaleModel> sales = saleService.findAllById(voucherIds);
        if (!CollectionUtils.isEmpty(sales)) {
            for (SaleModel saleModel : sales) {
                saleModel.setTotalQuantityUsed(saleModel.getTotalQuantityUsed() - 1);
            }

            saleService.saveAll(sales);
        }
    }

    @Override
    public String uploadBillImage(Long id, String image, String imageLink) {
        OrderModel orderModel = findOne(id);
        if (orderModel == null) {
            throw new ResourceNotFoundException(String.format("Not found order by id: %s", id));
        }
        orderModel.setPaymentImage(image);
        orderRepository.save(orderModel);

        NhanhVNOrderUpdateResponse nhanhVNOrderUpdateResponse = nhanhVNOrderService
                .update(buildNhanhVNOrderUpdatePrivateDescriptionRequest(orderModel, imageLink));
        if (nhanhVNOrderUpdateResponse.getData() == null) {
            throw new BadRequestException(
                    nhanhVNOrderUpdateResponse.getMessages().toString().substring(1, nhanhVNOrderUpdateResponse.getMessages().toString().length() - 1));
        }

        return imageLink;
    }

}
