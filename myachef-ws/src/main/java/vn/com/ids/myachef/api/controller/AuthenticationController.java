package vn.com.ids.myachef.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.api.security.jwt.JWTTokenService;
import vn.com.ids.myachef.api.security.jwt.JWTTokenService.JwtTokenType;
import vn.com.ids.myachef.api.security.request.AppLoginRequest;
import vn.com.ids.myachef.api.security.request.WebLoginRequest;
import vn.com.ids.myachef.api.security.response.JwtResponse;
import vn.com.ids.myachef.api.security.response.RefreshTokenResponse;
import vn.com.ids.myachef.api.security.userdetails.UserDetailsImpl;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.exception.error.UnauthorizedException;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.dao.enums.CustomerStatus;
import vn.com.ids.myachef.dao.enums.UserStatus;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.UserModel;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private JWTTokenService jwtTokenService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @PostMapping("/web/signin")
    public JwtResponse signinByWeb(HttpServletRequest request, @Valid @RequestBody WebLoginRequest loginRequest) {
        log.info("------------------ WEB Login - START ----------------");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest, null));
        log.info("------------------ WEB Login - END ----------------");
        return buildJwtResponse(authentication);
    }

    @PostMapping("/app/signin")
    public JwtResponse signinByApp(HttpServletRequest request, @RequestBody AppLoginRequest loginRequest) {
        log.info("------------------ APP Login - START ----------------");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest, null));
        log.info("------------------ APP Login - START ----------------");
        return buildJwtResponse(authentication);
    }

    private JwtResponse buildJwtResponse(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtTokenService.generateJwtToken(userDetails, JwtTokenType.TOKEN);
        String refreshToken = jwtTokenService.generateJwtToken(userDetails, JwtTokenType.REFRESH);

        return new JwtResponse(userDetails, jwtToken, refreshToken);
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshToken(@RequestBody @NotBlank String refreshToken) {
        log.info("------------------ RefreshToken - START ----------------");
        if (!jwtTokenService.validateJwtToken(refreshToken)) {
            throw new UnauthorizedException("Invalid JWT token");
        }
        if (jwtTokenService.isTokenExpired(refreshToken)) {
            throw new UnauthorizedException("JWT token is expired");
        }

        Long userId = jwtTokenService.getUserId(refreshToken);

        String username = null;
        String type = null;

        if (UserDetailsImpl.CUSTOMER_ROLE.equals(jwtTokenService.getType(refreshToken))) {
            CustomerModel customerModel = customerService.findOne(userId);
            if (customerModel == null || customerModel.getStatus() != CustomerStatus.ACTIVE) {
                throw new ResourceNotFoundException("Customer not found or In_valid Customer");
            }
            username = customerModel.getUsername();
            type = UserDetailsImpl.CUSTOMER_ROLE;
        } else {
            UserModel userModel = userService.findOne(userId);
            if (userModel == null || userModel.getStatus() != UserStatus.ACTIVE) {
                throw new ResourceNotFoundException("User not found or In_valid User");
            }
            username = userModel.getUsername();
        }
        
        if (username == null) {
            throw new ResourceNotFoundException("Invalid User or Customer");
        }

        String jwtToken = jwtTokenService.generateJwtToken(userId.toString(), username, type, applicationConfig.getJwtExpirationMs());
        String newRefreshToken = jwtTokenService.generateJwtToken(userId.toString(), username, type, applicationConfig.getJwtRefreshTokenExpirationMs());

        RefreshTokenResponse refreshTokenResponse = buildRefreshTokenResponse(refreshToken, jwtToken, newRefreshToken);

        log.info("------------------ RefreshToken - END ----------------");
        return refreshTokenResponse;
    }

    public RefreshTokenResponse buildRefreshTokenResponse(String refreshToken, String jwtToken, String newRefreshToken) {
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
        refreshTokenResponse.setAccessToken(jwtToken);
        refreshTokenResponse.setRefreshToken(refreshToken);
        if (Boolean.TRUE.equals(jwtTokenService.isTokenExpired(refreshToken))) {
            refreshTokenResponse.setRefreshToken(newRefreshToken);
        }
        return refreshTokenResponse;
    }

    @PostMapping("/signout")
    public String signout() {
        log.info("------------------ Signout - START ----------------");
        UserDetailsImpl userDetails = securityContextService.getUserDetail();
        SecurityContextHolder.clearContext();

        log.info("Log out for User with username: {} successful!", userDetails.getUsername());
        return "Log out successful!";
    }

}
