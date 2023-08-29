package vn.com.ids.myachef.business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Configuration
@PropertySource(value = "classpath:momo.properties")
public class MomoConfiguration {

    @Value("${momo.partner.code}")
    private String momoPartnerCode;

    @Value("${momo.public.key}")
    private String momoPublicKey;

    @Value("${momo.pay.url}")
    private String momoPayUrl;

    @Value("${momo.confirm.url}")
    private String momoConfirmUrl;

    @Value("${momo.api.payment.version}")
    private Double momoApiPaymentVersion;

    @Value("${momo.api.payment.type}")
    private Integer momoApiPaymentType;
}
