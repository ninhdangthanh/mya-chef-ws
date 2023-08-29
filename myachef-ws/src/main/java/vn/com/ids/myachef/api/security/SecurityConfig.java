package vn.com.ids.myachef.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import vn.com.ids.myachef.api.security.filter.JwtAuthenticationEntryPoint;
import vn.com.ids.myachef.api.security.filter.JwtRequestFilter;
import vn.com.ids.myachef.api.security.provider.AuthenticationProviderImpl;
import vn.com.ids.myachef.api.security.userdetails.UserDetailsImpl;
import vn.com.ids.myachef.dao.enums.UserRole;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String API_SUBSCRIPTIONS_FIND_ALL = "/api/subscription/find-all";
    private static final String API_PRODUCTS = "/api/products/**";
    private static final String API_USER = "/api/users/**";

    private static final String API_AUTH = "/auth/**";

    private static final String ZALO_UPLOAD_FILE = "/upload/media";

    private static final String API_CATEGORIES = "/api/product-category-config/**";

    private static final String API_BANKS = "/api/bank-account/**";
    private static final String API_BANNERS = "/api/banners/**";
    private static final String API_CARTS = "/api/carts/**";
    private static final String API_CHARTS = "/api/charts/**";
    private static final String API_CUSTOMERS = "/api/customers/**";
    private static final String API_FREQUENTLY_ASKED_QUESTIONS = "/api/frequently-asked-questions/**";
    private static final String API_HOMES = "/api/home-config/**";
    private static final String API_MEMBERSHIP_LEVELS = "/api/membership-level/**";
    private static final String API_NHANHVN = "/api/nhanh-vn/**";
    private static final String API_NOTIFICATION = "/api/notification/**";
    private static final String API_ORDER = "/api/orders/**";
    private static final String API_SALE = "/api/sale/**";
    private static final String API_SHIPPING = "/api/shipping/**";
    private static final String API_INFOMATIONS = "/api/store-infomations/**";
    private static final String API_SUBSCRIPTION = "/api/subscription/**";
    private static final String API_SUBSCRIPTION_CUSTOMER = "/api/subscription-customer-detail-controller/**";
    private static final String API_SYSTEM_CONFIG = "/api/system-config/**";
    private static final String API_ZALO_UPLOAD = "/api/upload/**";

    @Autowired
    private AuthenticationProviderImpl authenticationProviderImpl;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public JwtRequestFilter jwtAuthenticationRequestFilter() {
        return new JwtRequestFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProviderImpl);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        http.authorizeRequests()//
//                // product //
//                .antMatchers(HttpMethod.POST, API_PRODUCTS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_PRODUCTS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.DELETE, API_PRODUCTS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_PRODUCTS).permitAll()//
//                // category //
//                .antMatchers(HttpMethod.POST, API_CATEGORIES).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_CATEGORIES).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.DELETE, API_CATEGORIES).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_CATEGORIES).permitAll()//
//                // auth //
//                .antMatchers(API_AUTH).permitAll()//
//                // user //
//                .antMatchers(HttpMethod.POST, API_USER).hasAnyAuthority(UserRole.ADMIN.name())//
//                .antMatchers(HttpMethod.DELETE, API_USER).hasAnyAuthority(UserRole.ADMIN.name())//
//                .antMatchers(HttpMethod.PATCH, API_USER).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_USER).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                // bank //
//                .antMatchers(HttpMethod.POST, API_BANKS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_BANKS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.DELETE, API_BANKS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_BANKS).permitAll()//
//                // banner //
//                .antMatchers(HttpMethod.POST, API_BANNERS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_BANNERS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.DELETE, API_BANNERS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_BANNERS).permitAll()//
//                // cart //
//                .antMatchers(API_CARTS).permitAll()//
//                // chart //
//                .antMatchers(API_CHARTS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                // customer //
//                .antMatchers(HttpMethod.PATCH, API_CUSTOMERS).hasAnyAuthority(UserDetailsImpl.CUSTOMER_ROLE, UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_CUSTOMERS).permitAll()//
//                // frequently asked questions //
//                .antMatchers(HttpMethod.POST, API_FREQUENTLY_ASKED_QUESTIONS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_FREQUENTLY_ASKED_QUESTIONS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.DELETE, API_FREQUENTLY_ASKED_QUESTIONS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_FREQUENTLY_ASKED_QUESTIONS).permitAll()//
//                // home //
//                .antMatchers(HttpMethod.PATCH, API_HOMES).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_HOMES).permitAll()//
//                // membership //
//                .antMatchers(HttpMethod.POST, API_MEMBERSHIP_LEVELS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_MEMBERSHIP_LEVELS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.DELETE, API_MEMBERSHIP_LEVELS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_MEMBERSHIP_LEVELS).permitAll()//
//                // nhanh vn//
//                .antMatchers(API_NHANHVN).permitAll() //
//                // .antMatchers(HttpMethod.POST, API_NHANHVN).hasAnyAuthority(UserRole.ADMIN.name())//
//                // .antMatchers(HttpMethod.PATCH, API_NHANHVN).hasAnyAuthority(UserRole.ADMIN.name())//
//                // .antMatchers(HttpMethod.PUT, API_NHANHVN).hasAnyAuthority(UserRole.ADMIN.name())//
//                // .antMatchers(HttpMethod.GET, API_NHANHVN).permitAll()//
//                // notification //
//                .antMatchers(API_NOTIFICATION).permitAll()//
//                // order //
//                .antMatchers(HttpMethod.POST, API_ORDER).hasAnyAuthority(UserDetailsImpl.CUSTOMER_ROLE)//
//                .antMatchers(HttpMethod.PATCH, API_ORDER).hasAnyAuthority(UserDetailsImpl.CUSTOMER_ROLE, UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_ORDER).permitAll()//
//                // sale //
//                .antMatchers(HttpMethod.POST, API_SALE).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PUT, API_SALE).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.DELETE, API_SALE).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_SALE).permitAll()//
//                // shipping //
//                .antMatchers(API_SHIPPING).permitAll()//
//                // store infomation //
//                .antMatchers(HttpMethod.POST, API_INFOMATIONS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_INFOMATIONS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.DELETE, API_INFOMATIONS).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_INFOMATIONS).permitAll()//
//                // subscription //
//                .antMatchers(HttpMethod.POST, API_SUBSCRIPTION).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_SUBSCRIPTION).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PUT, API_SUBSCRIPTION).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_SUBSCRIPTION).permitAll()//
//                // subscription customer detail //
//                .antMatchers(HttpMethod.POST, API_SUBSCRIPTION_CUSTOMER).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PATCH, API_SUBSCRIPTION_CUSTOMER).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.PUT, API_SUBSCRIPTION_CUSTOMER).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_SUBSCRIPTION_CUSTOMER).permitAll()//
//                // system_config //
//                .antMatchers(HttpMethod.PATCH, API_SYSTEM_CONFIG).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())//
//                .antMatchers(HttpMethod.GET, API_SYSTEM_CONFIG).permitAll()//
//                // zalo upload //
//                .antMatchers(API_ZALO_UPLOAD).permitAll()//
//                // //
//                .anyRequest().authenticated()//
//                .and() //
//                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
//
//        http.addFilterBefore(jwtAuthenticationRequestFilter(), UsernamePasswordAuthenticationFilter.class);
         http.authorizeRequests().anyRequest().permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui/**", //
                "/v3/api-docs/**", //
                "/configuration/ui", //
                "/swagger-resources/**", //
                "/configuration/security", //
                "/webjars/**", //
                "/css/**", "/font/**", "/img/**", "/js/**", "/scss/**", "/vendor/**", //
                "/data/**", "/stomp-endpoint/**");
    }
}
