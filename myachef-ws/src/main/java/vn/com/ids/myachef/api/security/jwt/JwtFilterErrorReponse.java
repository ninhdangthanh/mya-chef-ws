package vn.com.ids.myachef.api.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtFilterErrorReponse {

    private Integer code;
    private String message;

}
