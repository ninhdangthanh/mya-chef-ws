package vn.com.ids.myachef.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.com.ids.myachef.business.cwp.client.CWPDomainService;

@Configuration
public class CWPDomainConfig {

    @Bean
    public CWPDomainService cwpDomainService() {
        return new CWPDomainService();
    }
}
