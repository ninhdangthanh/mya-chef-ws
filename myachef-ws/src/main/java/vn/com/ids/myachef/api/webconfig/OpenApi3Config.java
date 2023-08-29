package vn.com.ids.myachef.api.webconfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import vn.com.ids.myachef.business.constant.APIConstant;

@Configuration
public class OpenApi3Config {

    @Bean
    public OpenAPI customOpenAPI(@Value("${server.servlet.context-path}") String serverUrl) {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI() //
                .servers(Arrays.asList( //
                new Server().url(serverUrl))) //
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)) // enable auth button
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))) //
                .info(new Info() //
                        .title("IDS CRM Restful Api") //
                        .version("1.0") //
                        .description("IDS CRM Restful Api Description") //
                        .termsOfService("http://swagger.io/terms/") //
                        .contact(new Contact().email("helpdesk@idsolutions.com.vn").name("IDS Helpdesk Team"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")).version("1.0"));
    }
    
    static {
        Schema<LocalTime> timeSchema = new Schema<>();
        timeSchema.example(LocalTime.now().format(DateTimeFormatter.ofPattern(APIConstant.HH_MM_PATTERN)));
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, timeSchema);
        
        Schema<LocalDate> dateSchema = new Schema<>();
        dateSchema.example(LocalDate.now().format(DateTimeFormatter.ofPattern(APIConstant.YYYY_MM_DD_PATTERN)));
        SpringDocUtils.getConfig().replaceWithSchema(LocalDate.class, dateSchema);
        
        Schema<LocalDateTime> datetimeSchema = new Schema<>();
        datetimeSchema.example(LocalDateTime.now().format(DateTimeFormatter.ofPattern(APIConstant.YYYY_MM_DD_T_HH_MM_SS_PATTERN)));
        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class, datetimeSchema);
    }
    
}
