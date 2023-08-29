package vn.com.ids.myachef.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import vn.com.ids.myachef.api.security.userdetails.UserDetailsImpl;
import vn.com.ids.myachef.business.exception.error.UnauthorizedException;

@Component
public class SecurityContextService {

    // get user logged from SecurityContextHolder
    public UserDetailsImpl getUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().getClass() != UserDetailsImpl.class) {
            throw new UnauthorizedException("Login to use this function!");
        }
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    // get id of authenticated user
    public Long getAuthenticatedUserId() {
        return getUserDetail().getId();
    }

}
