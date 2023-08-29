package vn.com.ids.myachef.business.payload.reponse.zaloprofile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileResponse {

    public UserProfileData data;
    public int error;
    public String message;

}