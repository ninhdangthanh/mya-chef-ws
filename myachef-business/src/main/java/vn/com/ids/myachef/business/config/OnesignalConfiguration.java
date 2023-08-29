package vn.com.ids.myachef.business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Configuration
@PropertySource(value = "classpath:onesignal.properties")
public class OnesignalConfiguration {

    // OneSignal WEB
    @Value("${onesignal.api.key}")
    private String oneSignalApiKey;

    @Value("${onesignal.app.id}")
    private String oneSignalAppId;

    @Value("${onesignal.zalo.assistant.api.key}")
    private String oneSignalZaloAssistantApiKey;

    // OneSignal Mobile App
    @Value("${onesignal.zalo.assistant.app.id}")
    private String oneSignalZaloAssistantAppId;

    @Value("${onesignal.zalo.assistant.push.url}")
    private String onesignalZaloAssistantPushUrl;
}
