package vn.com.ids.myachef.business.zalo.social;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfile {
    public UserData data;
    public int error;
    public String message;
}
