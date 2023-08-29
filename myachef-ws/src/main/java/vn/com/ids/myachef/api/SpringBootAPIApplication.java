package vn.com.ids.myachef.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "vn.com.ids.myachef")
public class SpringBootAPIApplication extends SpringBootServletInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootAPIApplication.class);

    public static void main(String[] args) {
        LOGGER.info("*******************************************");
        LOGGER.info("******* MYA-CHEF Starting ******************");
        LOGGER.info("*******************************************");
        SpringApplication.run(SpringBootAPIApplication.class);
        LOGGER.info("*******************************************");
        LOGGER.info("******* MYA-CHEF successfully ********");
        LOGGER.info("*******************************************");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootAPIApplication.class);
    }

}
