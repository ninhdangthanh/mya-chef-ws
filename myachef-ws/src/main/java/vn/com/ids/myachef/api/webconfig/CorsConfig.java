package vn.com.ids.myachef.api.webconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import vn.com.ids.myachef.business.config.ApplicationConfig;

@Configuration
public class CorsConfig {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**") //
                        .allowedOrigins(applicationConfig.getCorsAllowedDomains()) //
                        .allowedHeaders("*") //
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD"); //
                // .allowCredentials(true);
            }
        };
    }
}