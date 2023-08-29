package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnAccessTokenResponse {

    private int code;

    private String accessToken;

    private String businessId;

    private LocalDateTime expiredDate;

    private List<String> depotIds;

    private List<String> permissions;

    private String message;
}
