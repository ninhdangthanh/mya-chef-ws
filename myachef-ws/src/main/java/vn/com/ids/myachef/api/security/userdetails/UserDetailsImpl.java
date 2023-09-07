package vn.com.ids.myachef.api.security.userdetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import vn.com.ids.myachef.dao.enums.UserRole;
import vn.com.ids.myachef.dao.model.UserModel;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    public static final String CUSTOMER_ROLE = "CUSTOMER";

    private Long id;
    private String username;
    private UserRole role;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UserModel userModel) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userModel.getRole().name()));

        return new UserDetailsImpl( //
                userModel.getId(), //
                userModel.getUsername(), //
                authorities);
    }

    // public static UserDetailsImpl build(CustomerModel customerModel) {
    // List<GrantedAuthority> authorities = new ArrayList<>();
    // authorities.add(new SimpleGrantedAuthority(CUSTOMER_ROLE));
    //
    // return new UserDetailsImpl( //
    // customerModel.getId(), //
    // customerModel.getUsername(), //
    // authorities);
    // }

    public Long getId() {
        return id;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return "hehe-boy";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorities, id, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDetailsImpl other = (UserDetailsImpl) obj;
        return Objects.equals(authorities, other.authorities) && Objects.equals(id, other.id) && Objects.equals(username, other.username);
    }

}
