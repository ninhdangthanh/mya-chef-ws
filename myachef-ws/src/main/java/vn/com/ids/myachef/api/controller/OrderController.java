package vn.com.ids.myachef.api.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.business.dto.OrderDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.payload.reponse.order.CalculateResponse;
import vn.com.ids.myachef.business.payload.request.CalculateRequest;
import vn.com.ids.myachef.business.service.CartService;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SystemConfigService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.OrderCriteria;
import vn.com.ids.myachef.dao.model.ProductConfigModel;
import vn.com.ids.myachef.dao.model.SystemConfigModel;
import vn.com.ids.myachef.dao.repository.extended.model.CartProduct;

@RestController
@RequestMapping("/api/orders")
@Slf4j
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private CartService cartService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private HttpServletRequest request;

    @Operation(summary = "Find By Admin Criteria")
    @GetMapping("/admin/search")
    public Page<OrderDTO> findByCriteriaForAdmin(@ParameterObject OrderCriteria orderCriteria) {
        log.info("------------------ OrderController - find By Admin Criteria - START ----------------");
        Page<OrderDTO> page = orderService.findByCriteriaForAdmin(orderCriteria, request);
        log.info("------------------ OrderController - find By Admin Criteria - END ----------------");
        return page;
    }

    @Operation(summary = "Find By User Criteria")
    @GetMapping("/user/search")
    public Page<OrderDTO> findByCriteriaForUser(@ParameterObject OrderCriteria orderCriteria) {
        log.info("------------------ OrderController - Find by criteria - START ----------------");
        orderCriteria.setCustomerIds(Arrays.asList(securityContextService.getAuthenticatedUserId()));
        Page<OrderDTO> page = orderService.findByCriteriaForUser(orderCriteria, request);
        log.info("------------------ OrderController - Find by criteria - END ----------------");
        return page;
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public OrderDTO findbyId(@PathVariable("id") Long id) {
        log.info("------------------ OrderController - findById - START ----------------");
        OrderDTO orderDTO = orderService.findById(id, request);
        log.info("------------------ OrderController - findById - END ----------------");
        return orderDTO;
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public OrderDTO create(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("------------------ OrderController - Create - START ----------------");
        OrderDTO orderResponse = orderService.create(orderDTO, securityContextService.getAuthenticatedUserId(), request);
        log.info("------------------ OrderController - Create - END ----------------");
        return orderResponse;
    }

    // @Operation(summary = "Upload Payment Transfer Image")
    // @PatchMapping(value = "/upload-payment-transfer-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // public String uploadPaymentTransferImage(@PathVariable Long id, @RequestParam(value = "image", required = true)
    // MultipartFile image) {
    // log.info("------------------ OrderController - uploadPaymentTransferImage - START ----------------");
    // String imageLink = orderService.uploadPaymentTransferImage(id, image);
    // log.info("------------------ OrderController - uploadPaymentTransferImage - END ----------------");
    //
    // return imageLink;
    // }

    @Operation(summary = "Canceled")
    @PatchMapping("/cancel/{nhanhVnId}")
    public String cancel(@PathVariable String nhanhVnId) {
        log.info("------------------ OrderController - Update - START ----------------");
        String message = orderService.cancel(nhanhVnId);
        log.info("------------------ OrderController - Update - END ----------------");
        return message;
    }

    @Operation(summary = "Reorder")
    @PostMapping("/re-order/{nhanhVnId}")
    public String reorder(@PathVariable String nhanhVnId) {
        log.info("------------------ OrderController - Reorder - START ----------------");
        String message = orderService.reorder(nhanhVnId, securityContextService.getAuthenticatedUserId());
        log.info("------------------ OrderController - Reorder - END ----------------");
        return message;
    }

    @Operation(summary = "Upload Payment Transfer Image")
    @PatchMapping(value = "/upload/bill/{id}")
    public String uploadPaymentTransferImage(@PathVariable Long id, //
            @RequestParam(value = "imageName", required = true) String image, //
            @RequestParam(value = "imageLink", required = true) String link) {
        log.info("------------------ OrderController - uploadPaymentTransferImage - START ----------------");
        String message = orderService.uploadBillImage(id, image, link);
        log.info("------------------ OrderController - uploadPaymentTransferImage - END ----------------");
        return message;
    }

    @Operation(summary = "Calculate")
    @PostMapping("/calculate")
    public CalculateResponse calculate(@RequestBody CalculateRequest calculateRequest) {
        log.info("------------------ OrderController - Calculate - START ----------------");

        SystemConfigModel systemConfig = systemConfigService.findAll().stream().findFirst().orElseThrow(() -> {
            throw new ResourceNotFoundException("Not found SystemConfig");
        });
        CalculateResponse calculateResponse = null;
        if (!CollectionUtils.isEmpty(calculateRequest.getCartIds())) {
            List<CartProduct> cartProducts = cartService.findQuantityAndProductByCartIdIn(calculateRequest.getCartIds());
            if (!CollectionUtils.isEmpty(cartProducts)) {
                calculateResponse = orderService.calculate(cartProducts, null, 0, securityContextService.getAuthenticatedUserId(),
                        calculateRequest.getVoucherIds(), systemConfig, calculateRequest.getSubscriptionGifts(), calculateRequest.getCartIds(),
                        calculateRequest.getNhanhVnProductId(), calculateRequest.getCoin());
            }
        } else if (calculateRequest.getNhanhVnProductId() != null && calculateRequest.getQuantity() > 0) {
            ProductConfigModel product = productConfigService.findByNhanhVnId(calculateRequest.getNhanhVnProductId());
            if (product != null) {
                calculateResponse = orderService.calculate(null, product, calculateRequest.getQuantity(), securityContextService.getAuthenticatedUserId(),
                        calculateRequest.getVoucherIds(), systemConfig, calculateRequest.getSubscriptionGifts(), calculateRequest.getCartIds(),
                        calculateRequest.getNhanhVnProductId(), calculateRequest.getCoin());
            } else {
                throw new ResourceNotFoundException("Not found product by id: " + calculateRequest.getNhanhVnProductId());
            }
        } else {
            throw new BadRequestException("Field cartIds or (nhanhVnProductId and quantity) is required");
        }
        log.info("------------------ OrderController - Calculate - END ----------------");
        return calculateResponse;
    }

    // @Operation(summary = "Delete")
    // @DeleteMapping()
    // public void delete(@RequestParam List<Long> ids) {
    // log.info("------------------ OrderController - Delete - START ----------------");
    // if (!CollectionUtils.isEmpty(ids)) {
    // orderService.deleteByIds(ids);
    // }
    // log.info("------------------ OrderController - Delete - END ----------------");
    // }

}
