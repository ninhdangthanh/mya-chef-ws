package vn.com.ids.myachef.business.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.MomoPaymentConverter;
import vn.com.ids.myachef.business.payload.reponse.momo.MomoCreatePaymentResponse;
import vn.com.ids.myachef.business.payload.reponse.momo.MomoDetailResponse;
import vn.com.ids.myachef.business.service.MomoAPIService;
import vn.com.ids.myachef.business.service.MomoPaymentService;
import vn.com.ids.myachef.dao.enums.MomoPaymentStatus;
import vn.com.ids.myachef.dao.enums.PaymentSource;
import vn.com.ids.myachef.dao.model.MomoPaymentModel;

@Transactional
@Service
@Slf4j
public class MomoAPIServiceImpl implements MomoAPIService {

    @Autowired
    private MomoPaymentConverter momoPaymentConverter;

    @Autowired
    private MomoPaymentService momoPaymentService;

    @Value("${momo.partner.code}")
    private String partnerCode;

    @Value("${momo.create.request.type}")
    private String createRequestType;

    @Value("${momo.ipn.url}")
    private String ipnUrl;

    @Value("${momo.redirect.url}")
    private String redirectUrl;

    @Value("${momo.order.info}")
    private String orderInfo;

    @Value("${momo.access.key}")
    private String accessKey;

    @Value("${momo.secret.key}")
    private String secretKey;

    @Value("${momo.base.url}")
    private String baseUrl;

    @Value("${momo.create.payment.request.url}")
    private String createPaymentRequestUrl;

    @Value("${momo.payment.status.url}")
    private String paymentStatusUrl;

    @Override
    public MomoPaymentModel createPaymentRequest(Long id, PaymentSource paymentSource, Long price, String returnUrl) {
        log.info("------------------ Momo createPaymentRequest - START ----------------");
        String extraData = Base64.getEncoder().encodeToString("{\r\n\"email\": \"huongxd@gmail.com\"\r\n}".getBytes());
        String uuid = UUID.randomUUID().toString();
        String orderId = uuid + ":" + id;
        String requestId = uuid + ":" + id;

        String signature = "accessKey=" + accessKey + "&amount=" + price + "&extraData=" + extraData + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo + "&partnerCode=" + partnerCode + "&redirectUrl=" + returnUrl + "&requestId=" + requestId + "&requestType="
                + createRequestType;
        signature = buildSignature(signature, secretKey);

        MomoCreatePaymentResponse response = Unirest.post(baseUrl + createPaymentRequestUrl) //
                .header("Content-Type", "application/json") //
                .body("{\"partnerCode\": \"" + partnerCode + "\"," //
                        + "\"partnerName\": \"" + "Thanh toán " + paymentSource + "\"," //
                        + "\"storeId\": \"" + partnerCode + "\"," //
                        + "\"requestType\": \"" + createRequestType + "\"," //
                        + "\"ipnUrl\": \"" + ipnUrl + "\"," //
                        + "\"redirectUrl\": \"" + returnUrl + "\"," //
                        + "\"orderId\": \"" + orderId + "\"," //
                        + "\"amount\": \"" + price + "\"," //
                        + "\"lang\": \"vi\"," //
                        + "\"autoCapture\": true," //
                        + "\"orderInfo\": \"" + orderInfo + "\"," //
                        + "\"requestId\": \"" + requestId + "\"," //
                        + "\"extraData\": \"" + extraData + "\"," //
                        + "\"signature\": \"" + signature + "\"}")
                .asObject(MomoCreatePaymentResponse.class) //
                .getBody();

        if (response.getResultCode() == 0) {
            MomoPaymentModel momoPaymentModel = momoPaymentConverter.toMomoPaymentModel(response);
            momoPaymentModel.setStatus(MomoPaymentStatus.PENDING);
            momoPaymentService.save(momoPaymentModel);
            return momoPaymentModel;
        } else {
            log.error("Momo create payment requets error: {}", response.getMessage());
        }

        return null;
    }

    public String buildSignature(String signature, String secretKey) {
        String result = "";
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            byte[] hash = hmacSHA256.doFinal(signature.getBytes());

            // Convert hash bytes to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            try (Formatter formatter = new Formatter(hexString)) {
                for (byte b : hash) {
                    formatter.format("%02x", b);
                }
            }

            result = hexString.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public MomoDetailResponse getPaymentStatus(String requestId) {
        String signature = "accessKey=" + accessKey + "&orderId=" + requestId + "&partnerCode=" + partnerCode + "&requestId=" + requestId;
        signature = buildSignature(signature, secretKey);

        return Unirest.post(baseUrl + paymentStatusUrl) //
                .header("Content-Type", "application/json") //
                .body("{\"partnerCode\": \"" + partnerCode + "\"," //
                        + "\"requestId\": \"" + requestId + "\"," //
                        + "\"orderId\": \"" + requestId + "\"," //
                        + "\"lang\": \"vi\"," //
                        + "\"signature\": \"" + signature + "\"}")
                .asObject(MomoDetailResponse.class) //
                .getBody();
    }

    @Override
    public void refund(String requestId, String transId, Long price, String description) {
        if (transId == null) {
            MomoDetailResponse momoPaymentStatusResponse = getPaymentStatus(requestId);
            if (momoPaymentStatusResponse != null && momoPaymentStatusResponse.getResultCode() == 0) {
                transId = momoPaymentStatusResponse.getTransId();
            } else {
                log.error("Error when get payment status message");
            }

            if (transId != null) {
                String orderId = UUID.randomUUID().toString() + ":" + requestId.split(":")[1];

                String signature = "accessKey=" + accessKey + "&amount=" + price + "&description=" + description + "&orderId=" + orderId + "&partnerCode="
                        + partnerCode + "&requestId=" + requestId + "&transId=" + transId;
                signature = buildSignature(signature, secretKey);

                MomoDetailResponse response = Unirest.post("https://test-payment.momo.vn/v2/gateway/api/refund") //
                        .header("Content-Type", "application/json") //
                        .body("{\"partnerCode\": \"" + partnerCode + "\"," //
                                + "\"orderId\": \"" + orderId + "\"," //
                                + "\"requestId\": \"" + requestId + "\"," //
                                + "\"amount\": \"" + price + "\"," //
                                + "\"transId\":  \"" + transId + "\"," //
                                + "\"lang\":  \"vi\"," //
                                + "\"description\":  \"" + description + "\"," //
                                + "\"signature\": \"" + signature + "\"\n}") //
                        .asObject(MomoDetailResponse.class).getBody();

                MomoPaymentModel momoPaymentModel = momoPaymentService.findByRequestId(requestId);
                if (momoPaymentModel != null) {

                    momoPaymentModel.setResultCode(response.getResultCode());
                    momoPaymentModel.setMessage(response.getMessage());

                    if (response.getResultCode() == 0) {
                        momoPaymentModel.setStatus(MomoPaymentStatus.CANCEL);
                        log.error("Momo refund success");
                    } else {
                        momoPaymentModel.setStatus(MomoPaymentStatus.REFUND_FAILED);
                        log.error("Momo refund failed message {}", response.getMessage());
                    }

                    momoPaymentService.save(momoPaymentModel);
                } else {
                    log.error("Momo refund failed null");
                }
            }
        }
    }
}
