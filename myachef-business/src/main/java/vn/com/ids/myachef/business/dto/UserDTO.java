package vn.com.ids.myachef.business.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.business.validation.group.OnUpdate;
import vn.com.ids.myachef.dao.enums.UserRole;
import vn.com.ids.myachef.dao.enums.UserStatus;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    @NotNull(message = "Field 'id' can not be null", groups = OnUpdate.class)
    private Long id;

    @NotBlank(message = "Field 'username' can not be blank", groups = OnCreate.class)
    private String username;

    @NotBlank(message = "Field 'password': {notblank}", groups = OnCreate.class)
    @Size(min = 6)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    private String phoneNumber;

    private String fullName;

    private String address;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String avatarFile;

    private LocalDate birthday;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
