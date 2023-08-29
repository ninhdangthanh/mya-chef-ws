package vn.com.ids.myachef.business.payload.reponse.zaloprofile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileData {
    public String provider;
    public String latitude;
    public String longitude;
    public String timestamp;
    public String number;

}