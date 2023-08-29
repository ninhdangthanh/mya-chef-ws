package vn.com.ids.myachef.api.security.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebLoginRequest extends AbstractLoginRequest {
    private String username;
    private String password;
}
