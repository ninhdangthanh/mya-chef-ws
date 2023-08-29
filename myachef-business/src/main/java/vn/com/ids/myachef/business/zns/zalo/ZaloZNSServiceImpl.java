package vn.com.ids.myachef.business.zns.zalo;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.zns.ZNSAbstractFactory;
import vn.com.ids.myachef.business.zns.ZNSService;
import vn.com.ids.myachef.dao.enums.ZaloZNSChannel;

@Component
@Slf4j
public class ZaloZNSServiceImpl extends ZNSAbstractFactory implements ZNSService {

    @Override
    public ZaloZNSChannel getType() {
        return ZaloZNSChannel.ZALO;
    }

    @Override
    public boolean sendOTP(String phoneNumber, String otpCode) {
        log.info("----------- start send OTP ZALO -----------");
        // not implement yet
        return false;
    }

}
