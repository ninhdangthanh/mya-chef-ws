package vn.com.ids.myachef.business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Configuration
@PropertySource(value = "classpath:vihat.properties")
public class VihatConfiguration {

    @Value("${vihat.api.key}")
    private String vihatAPIKey;

    @Value("${vihat.secret.key}")
    private String vihatSecretKey;

    @Value("${vihat.otp.template}")
    private String vihatOTPtemplate;

    @Value("${vihat.otp.oaid}")
    private String vihatOTPOAId;

    @Value("${vihat.zalo.api}")
    private String vihatZaloApi;
}
