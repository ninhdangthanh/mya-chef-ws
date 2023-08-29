package vn.com.ids.myachef.api.security.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.jwt.JWTTokenService;
import vn.com.ids.myachef.api.security.jwt.JwtFilterErrorReponse;
import vn.com.ids.myachef.api.security.userdetails.UserDetailsImpl;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.dao.enums.CustomerStatus;
import vn.com.ids.myachef.dao.enums.UserStatus;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.UserModel;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String jwtToken = jwtTokenService.parseJwt(request);
        if (jwtToken != null) {
            if (!jwtTokenService.validateJwtToken(jwtToken)) {
                response.setStatus(401);
                JwtFilterErrorReponse jwtErrorReponse = new JwtFilterErrorReponse(401, "Invalid JWT token.");
                toJsonResponse(response, jwtErrorReponse);
                return;
            }
            if (jwtTokenService.isTokenExpired(jwtToken)) {
                response.setStatus(401);
                JwtFilterErrorReponse jwtErrorReponse = new JwtFilterErrorReponse(401, "JWT token is expired");
                toJsonResponse(response, jwtErrorReponse);
                return;
            }
            
            Long userId = jwtTokenService.getUserId(jwtToken);
            UserDetailsImpl userDetails = null;
            if (UserDetailsImpl.CUSTOMER_ROLE.equals(jwtTokenService.getType(jwtToken))) {
                CustomerModel customerModel = customerService.findOne(userId);
                if (customerModel == null || customerModel.getStatus() != CustomerStatus.ACTIVE) {
                    throw new ResourceNotFoundException("Customer not found or In_valid Customer");
                }
                userDetails = UserDetailsImpl.build(customerModel);
            } else {
                UserModel userModel = userService.findOne(userId);
                if (userModel == null || userModel.getStatus() != UserStatus.ACTIVE) {
                    throw new ResourceNotFoundException("User not found or In_valid User");
                }
                userDetails = UserDetailsImpl.build(userModel);
            }

            if (userDetails == null) {
                response.setStatus(401);
                JwtFilterErrorReponse jwtErrorReponse = new JwtFilterErrorReponse(401, "UserDetails is null");
                toJsonResponse(response, jwtErrorReponse);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private void toJsonResponse(HttpServletResponse response, Object data) {
        ObjectMapper mapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.println(mapper.writeValueAsString(data));
            out.flush();
        } catch (Exception e) {
            log.error("Response invalid json format： {}", e.getLocalizedMessage());
        }
    }
}
