package vn.com.ids.myachef.business.onesignal;

import java.util.List;

public interface OnesignalPushNotificationService {

    public String pushNotification(List<String> pushTokens, String redirectUrl, String message, String headings);

}
