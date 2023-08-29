package vn.com.ids.myachef.business.service.impl;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnAccessTokenResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductCategoryResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductDetailResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductWebHooks;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.UpdateOrderWebhooks;
import vn.com.ids.myachef.business.service.NhanhVnAPIService;
import vn.com.ids.myachef.business.service.NhanhVnService;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.dao.model.NhanhVnModel;

@Service
@Transactional
@Slf4j
public class NhanhVnAPIServiceImpl implements NhanhVnAPIService {

    private static final String WEBHOOKS_ENABLED = "webhooksEnabled";

    private static final String PRODUCT_UPDATE_EVENT = "productUpdate";

    private static final String ORDER_UPDATE_EVENT = "orderUpdate";

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private NhanhVnService nhanhVnService;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private OrderService orderService;

    @Override
    public void getAccessToken(String accessCode) {
        NhanhVnModel nhanhVnModel = nhanhVnService.findByBiggestPriority();

        if (nhanhVnModel != null) {
            NhanhVnAccessTokenResponse response = Unirest.post(applicationConfig.getNhanhAccessTokenUrl()) //
                    .multiPartContent() //
                    .field("version", nhanhVnModel.getVersion()) //
                    .field("appId", nhanhVnModel.getAppId()) //
                    .field("accessCode", accessCode) //
                    .field("secretKey", nhanhVnModel.getSecretKey()) //
                    .asObject(NhanhVnAccessTokenResponse.class).getBody();

            if (response.getCode() == 1) {
                nhanhVnModel.setAccessToken(response.getAccessToken());
                nhanhVnModel.setExpiredDate(response.getExpiredDate().minusDays(2));
                nhanhVnModel.setBusinessId(response.getBusinessId());

                nhanhVnService.save(nhanhVnModel);
            } else {
                log.error("Get access token failed message={}", response.getMessage());
            }
        } else {
            log.error("Get access token model null");
        }
    }

    @Override
    public ResponseEntity<?> receiveWebHooks(String data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(data);
            String eventType = rootNode.get("event").asText();
            Gson gson = new Gson();

            log.info("------------------ '" + eventType + "Time" + LocalDateTime.now() + "' ----------------");
            switch (eventType) {
            case ORDER_UPDATE_EVENT:
                log.info("Webhooks order update");
                UpdateOrderWebhooks updateOrderWebhooks = gson.fromJson(data, UpdateOrderWebhooks.class);
                orderService.updateStatusByNhanhWebhooks(updateOrderWebhooks.getData());
                break;

            case PRODUCT_UPDATE_EVENT:
                log.info("Webhooks product update");
                NhanhVnProductWebHooks nhanhVnProductWebHooks = gson.fromJson(data, NhanhVnProductWebHooks.class);
                productConfigService.updateProductInfoFromNhanhVn(nhanhVnProductWebHooks.getData());
                break;

            case WEBHOOKS_ENABLED:
                log.info("Webhooks webhooks enabled");
                break;

            default:
                break;
            }
        } catch (JsonProcessingException e) {
            log.info("WEBHOOKS ERROR {}", e.getMessage());
        }

        log.info("------------------ Nhanh vn web hooks callback url - END ----------------");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @Override
    public NhanhVnProductCategoryResponse getListCategory() {
        NhanhVnModel nhanhVnModel = getAccessToken();
        if (nhanhVnModel == null) {
            log.error("Access token does not exist or expired");
            return null;
        }

        return Unirest.post(applicationConfig.getNhanhGetAllProductCategoryUrl()) //
                .multiPartContent() //
                .field("version", nhanhVnModel.getVersion()) //
                .field("appId", nhanhVnModel.getAppId()) //
                .field("businessId", nhanhVnModel.getBusinessId()) //
                .field("accessToken", nhanhVnModel.getAccessToken()) //
                .asObject(NhanhVnProductCategoryResponse.class) //
                .getBody();
    }

    @Override
    public NhanhVnProductResponse getListProduct(String pagination) {
        NhanhVnModel nhanhVnModel = getAccessToken();
        if (nhanhVnModel == null) {
            log.error("Access token does not exist or expired");
            return null;
        }

        return Unirest.post(applicationConfig.getNhanhGetAllProductUrl()) //
                .multiPartContent() //
                .field("version", nhanhVnModel.getVersion()) //
                .field("appId", nhanhVnModel.getAppId()) //
                .field("businessId", nhanhVnModel.getBusinessId()) //
                .field("accessToken", nhanhVnModel.getAccessToken()) //
                .field("data", pagination) //
                .asObject(NhanhVnProductResponse.class) //
                .getBody();
    }

    public NhanhVnModel getAccessToken() {
        NhanhVnModel nhanhVnModel = nhanhVnService.findByBiggestPriority();
        if (StringUtils.hasText(nhanhVnModel.getAccessToken()) && nhanhVnModel.getExpiredDate().isAfter(LocalDateTime.now())) {
            return nhanhVnModel;
        } else {
            // send mail to admin for get access token

            return null;
        }
    }

    @Override
    public NhanhVnProductDetailResponse getById(String productId) {
        NhanhVnModel nhanhVnModel = getAccessToken();
        if (nhanhVnModel == null) {
            log.error("Access token does not exist or expired");
            return null;
        }

        String response = Unirest.post(applicationConfig.getNhanhDetailProduct()) //
                .multiPartContent() //
                .field("version", nhanhVnModel.getVersion()) //
                .field("appId", nhanhVnModel.getAppId()) //
                .field("businessId", nhanhVnModel.getBusinessId()) //
                .field("accessToken", nhanhVnModel.getAccessToken()) //
                .field("data", productId) //
                .asString() //
                .getBody();

        if (StringUtils.hasText(response)) {
            try {
                JSONObject responseJsonObject = new JSONObject(response);
                if (responseJsonObject.getInt("code") == 1) {
                    JSONObject jsonObject = new JSONObject(responseJsonObject.getString("data"));
                    String data = jsonObject.getString(productId);
                    Gson gson = new Gson();
                    return gson.fromJson(data, NhanhVnProductDetailResponse.class);
                } else {
                    log.error("Get by id from nhanh vn error message {} with id " + productId, responseJsonObject.getString("messages"));
                }
            } catch (JSONException ex) {
                log.error(ex.getMessage());
            }
        } else {
            log.error("Get by id from nhanh vn error null with id {}", productId);
        }

        return null;
    }
}
