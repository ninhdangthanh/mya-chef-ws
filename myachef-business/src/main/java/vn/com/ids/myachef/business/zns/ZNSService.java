package vn.com.ids.myachef.business.zns;

import vn.com.ids.myachef.dao.enums.ZaloZNSChannel;

public interface ZNSService {

    public ZaloZNSChannel getType();

    public boolean sendOTP(String phoneNumber, String otpCode);
}
