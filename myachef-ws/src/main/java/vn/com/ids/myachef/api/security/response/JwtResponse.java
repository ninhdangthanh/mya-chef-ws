package vn.com.ids.myachef.api.security.response;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.api.security.userdetails.UserDetailsImpl;

@Component
@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {

    private Long id;
    private String username;
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;

    public JwtResponse(UserDetailsImpl userDetails, String jwtToken, String refreshToken) {
        this.id = userDetails.getId();
        this.username = userDetails.getUsername();
        this.accessToken = jwtToken;
        this.refreshToken = refreshToken;
    }

}
