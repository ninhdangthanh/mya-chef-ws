package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class NhanhVnDTO {

    private Long id;

    @NotBlank(message = "Field 'appId': {notblank}", groups = { OnCreate.class })
    private String appId;

    private String businessId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secretKey;

    @NotBlank(message = "Field 'name': {version}", groups = { OnCreate.class })
    private String version;

    private String accessToken;

    private LocalDateTime expiredDate;

    private Integer priority;

    private Status status;
}
