package vn.com.ids.myachef.business.zalo.token;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZaloToken {

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("expires_in")
    public String expiresIn;

    @SerializedName("error_name")
    public String errorName;

    @SerializedName("error_reason")
    public String errorReason;

    @SerializedName("error_description")
    public String errorDescription;

    @SerializedName("error")
    public int errorCode;

}
