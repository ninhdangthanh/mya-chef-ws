package vn.com.ids.myachef.business.service.nhanhvn.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.payload.reponse.order.CalculateResponse;
import vn.com.ids.myachef.business.service.nhanhvn.NhanhVNOrderService;
import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVNOrderCreateRequest;
import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVNOrderUpdateRequest;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNOrderCreateResponse;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNOrderUpdateResponse;
import vn.com.ids.myachef.dao.model.NhanhVnModel;
import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.model.ProductConfigModel;

@Service
@Transactional
@Slf4j
public class NhanhVNOrderServiceImpl extends NhanhVNAbstractServiceImpl implements NhanhVNOrderService {

    @Override
    public NhanhVNOrderCreateResponse create(OrderModel orderModel, CalculateResponse calculateResponse) {
        NhanhVnModel nhanhVnModel = nhanhVnService.findByBiggestPriority();
        NhanhVNOrderCreateResponse nhanhVNOrderCreateResponse = null;
        if (nhanhVnModel != null) {
            String mesageObject = toJson(buildNhanhVNOrderCreateRequest(orderModel, calculateResponse));
            nhanhVNOrderCreateResponse = Unirest.post("https://open.nhanh.vn/api/order/add")//
                    .multiPartContent()//
                    .field(VERSION, nhanhVnModel.getVersion())//
                    .field(APP_ID, nhanhVnModel.getAppId())//
                    .field(BUSINESS_ID, nhanhVnModel.getBusinessId())//
                    .field(ACCESS_TOKEN, nhanhVnModel.getAccessToken())//
                    .field(DATA, mesageObject)//
                    .asObject(NhanhVNOrderCreateResponse.class)//
                    .getBody();

        } else {
            log.error("Can not create order Time is: " + LocalDateTime.now());
        }
        return nhanhVNOrderCreateResponse;
    }

    private NhanhVNOrderCreateRequest buildNhanhVNOrderCreateRequest(OrderModel orderModel, CalculateResponse calculateResponse) {
        NhanhVNOrderCreateRequest nhanhVNOrderCreateRequest = new NhanhVNOrderCreateRequest();
        // CustomerModel customer = orderModel.getCustomer();
        List<NhanhVNOrderCreateRequest.Product> products = new ArrayList<>();
        for (OrderDetailModel orderDetail : orderModel.getOrderDetails()) {
            ProductConfigModel productConfigModel = orderDetail.getProduct();
            NhanhVNOrderCreateRequest.Product product = nhanhVNOrderCreateRequest.new Product();
            product.setId(productConfigModel.getId().intValue());
            product.setIdNhanh(Integer.valueOf(productConfigModel.getNhanhVnId()));
            product.setQuantity(orderDetail.getQuantity());
            product.setName(productConfigModel.getName());
            product.setPrice(productConfigModel.getPrice().intValue());
            products.add(product);
        }

        nhanhVNOrderCreateRequest.setId(orderModel.getId().toString() + "-" + UUID.randomUUID().toString());
        nhanhVNOrderCreateRequest.setPaymentMethod(orderModel.getPaymentMethod().getValue());
        nhanhVNOrderCreateRequest.setCustomerName(orderModel.getRecipientName());
        nhanhVNOrderCreateRequest.setCustomerMobile(orderModel.getRecipientPhoneNumber());
        nhanhVNOrderCreateRequest.setCustomerCityName(orderModel.getRecipientCity());
        nhanhVNOrderCreateRequest.setCustomerDistrictName(orderModel.getRecipientDistrict());
        nhanhVNOrderCreateRequest.setCustomerWardLocationName(orderModel.getRecipientWard());
        nhanhVNOrderCreateRequest.setCustomerAddress(orderModel.getRecipientAddress());
        nhanhVNOrderCreateRequest.setCustomerShipFee((int) calculateResponse.getTotalShipFee());
        nhanhVNOrderCreateRequest.setDescription(orderModel.getNote());
        nhanhVNOrderCreateRequest.setCarrierId(24);
        nhanhVNOrderCreateRequest.setProductList(products);
        nhanhVNOrderCreateRequest.setMoneyDiscount(orderModel.getTotalMoneyDiscount());

        return nhanhVNOrderCreateRequest;
    }

    @Override
    public NhanhVNOrderUpdateResponse update(NhanhVNOrderUpdateRequest nhanhVNOrderUpdateRequest) {
        NhanhVnModel nhanhVnModel = nhanhVnService.findByBiggestPriority();

        NhanhVNOrderUpdateResponse nhanhVNOrderUpdateResponse = null;
        if (nhanhVnModel != null) {
            String mesageObject = toJson(nhanhVNOrderUpdateRequest);
            nhanhVNOrderUpdateResponse = update(nhanhVnModel, mesageObject);
        } else {
            log.error("Can not update Time is: " + LocalDateTime.now());
        }
        return nhanhVNOrderUpdateResponse;
    }

    private NhanhVNOrderUpdateResponse update(NhanhVnModel nhanhVnModel, String mesageObject) {
        NhanhVNOrderUpdateResponse nhanhVNOrderUpdateResponse;
        nhanhVNOrderUpdateResponse = Unirest.post("https://open.nhanh.vn/api/order/update")//
                .multiPartContent()//
                .field(VERSION, nhanhVnModel.getVersion())//
                .field(APP_ID, nhanhVnModel.getAppId())//
                .field(BUSINESS_ID, nhanhVnModel.getBusinessId())//
                .field(ACCESS_TOKEN, nhanhVnModel.getAccessToken())//
                .field(DATA, mesageObject)//
                .asObject(NhanhVNOrderUpdateResponse.class)//
                .getBody();
        return nhanhVNOrderUpdateResponse;
    }

}
