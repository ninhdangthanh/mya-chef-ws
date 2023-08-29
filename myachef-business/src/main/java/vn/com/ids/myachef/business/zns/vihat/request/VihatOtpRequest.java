package vn.com.ids.myachef.business.zns.vihat.request;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VihatOtpRequest {

    @SerializedName("ApiKey")
    private String apiKey;

    @SerializedName("SecretKey")
    private String secretKey;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Params")
    private List<String> params;

    @SerializedName("TempID")
    private String tempId;

    @SerializedName("OAID")
    private String oaId;

    @SerializedName("campaignid")
    private String campaignid;
    
    @SerializedName("RequestId")
    private String requestId;

    @SerializedName("CallbackUrl")
    private String callbackUrl;

}
