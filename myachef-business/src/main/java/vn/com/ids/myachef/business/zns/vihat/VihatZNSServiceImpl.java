package vn.com.ids.myachef.business.zns.vihat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.VihatConfiguration;
import vn.com.ids.myachef.business.zns.ZNSAbstractFactory;
import vn.com.ids.myachef.business.zns.ZNSService;
import vn.com.ids.myachef.business.zns.vihat.request.VihatOtpRequest;
import vn.com.ids.myachef.business.zns.vihat.response.VihatOtpResponse;
import vn.com.ids.myachef.dao.enums.ZaloZNSChannel;

@Component
@Slf4j
public class VihatZNSServiceImpl extends ZNSAbstractFactory implements ZNSService {

    @Autowired
    private VihatAPIService vihatService;

    @Autowired
    private VihatConfiguration vihatConfiguration;

    @Override
    public ZaloZNSChannel getType() {
        return ZaloZNSChannel.VIHAT;
    }

    @Override
    public boolean sendOTP(String phoneNumber, String otpCode) {
        log.info("----------- send otp START-----------");
        List<String> params = new ArrayList<>();
        params.add(otpCode);

        VihatOtpRequest request = new VihatOtpRequest();
        request.setApiKey(vihatConfiguration.getVihatAPIKey());
        request.setSecretKey(vihatConfiguration.getVihatSecretKey());
        request.setPhone(phoneNumber);
        request.setCampaignid("OTP Message");
        request.setOaId(vihatConfiguration.getVihatOTPOAId());
        request.setTempId(vihatConfiguration.getVihatOTPtemplate());

        request.setParams(params);

        Gson gson = initialize();
        String mesageObject = gson.toJson(request);

        VihatOtpResponse vihatOtpResponse = vihatService.message(mesageObject);
        boolean isSuccess = false;
        if (vihatOtpResponse == null) {
            log.error("Response is null");
        } else if (vihatOtpResponse.getCodeResult() == null) {
            log.error("CodeResult is null " + vihatOtpResponse.toString());
        } else if (vihatOtpResponse.getCodeResult().equals("100")) {
            isSuccess = true;
        }

        log.info("----------- send otp END-----------");

        // request is received and processed successfully
        return isSuccess;
    }

}
