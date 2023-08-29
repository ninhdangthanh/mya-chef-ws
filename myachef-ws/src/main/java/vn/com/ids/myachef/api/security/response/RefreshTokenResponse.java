package vn.com.ids.myachef.api.security.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenResponse {

    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    public RefreshTokenResponse() {
        super();
    }

    public RefreshTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
