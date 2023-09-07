package vn.com.ids.myachef.api.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.request.AbstractLoginRequest;
import vn.com.ids.myachef.api.security.request.AppLoginRequest;
import vn.com.ids.myachef.api.security.request.WebLoginRequest;
import vn.com.ids.myachef.api.security.userdetails.UserDetailsImpl;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.business.service.ZaloSocialService;
import vn.com.ids.myachef.dao.enums.UserStatus;
import vn.com.ids.myachef.dao.model.UserModel;

@Component
@Slf4j
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private ZaloSocialService zaloSocialService;

    // @Autowired
    // private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AbstractLoginRequest loginRequest = (AbstractLoginRequest) authentication.getPrincipal();

        UserDetailsImpl userDetailsImpl = null;
        if (loginRequest instanceof WebLoginRequest) {
            String username = ((WebLoginRequest) loginRequest).getUsername();
            String password = ((WebLoginRequest) loginRequest).getPassword();

            UserModel userModel = webLogin(username, password);
            userDetailsImpl = UserDetailsImpl.build(userModel);

        } else if (loginRequest instanceof AppLoginRequest) {
            // String zaloToken = ((AppLoginRequest) loginRequest).getToken();
            //
            // CustomerModel customerModel = appLogin(zaloToken);
            // userDetailsImpl = UserDetailsImpl.build(customerModel);
        }

        if (userDetailsImpl == null) {
            throw new BadCredentialsException("Bad credentials");
        }

        return new UsernamePasswordAuthenticationToken(userDetailsImpl, authentication.getCredentials(), userDetailsImpl.getAuthorities());
    }

    // private CustomerModel appLogin(String zaloToken) {
    // ZaloUser zaloUserInfo = zaloSocialService.getUserInfoByAccessToken(zaloToken);
    // if (zaloUserInfo == null || zaloUserInfo.getId() == null) {
    // throw new BadCredentialsException("Token In_valid or expired");
    // }
    // CustomerModel customerModel = customerService.findByUsername(zaloUserInfo.getId());
    // if (customerModel == null) {
    // customerModel = customerService.register(zaloUserInfo);
    // } else if (customerModel.getStatus() != CustomerStatus.ACTIVE) {
    // log.debug("User app: {} not found!", zaloUserInfo.getId());
    // throw new BadCredentialsException("Not found Customer or In_valid Customer");
    // }
    // customerModel.setZaloAccessToken(zaloToken);
    //
    // return customerService.save(customerModel);
    // }

    private UserModel webLogin(String username, String password) {
        UserModel userModel = userService.findByUsername(username);

        if (userModel == null || userModel.getStatus() != UserStatus.ACTIVE) {
            log.debug("User web: {} not found!", username);
            throw new ResourceNotFoundException("User not found or invalid user");
        }

        if (!this.passwordEncoder.matches(password, userModel.getPassword())) {
            log.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException("Bad credentials");
        }
        return userModel;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
