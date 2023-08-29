package vn.com.ids.myachef.business.zns.vihat.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VihatOtpResponse {
    @SerializedName("CodeResult")
    private String codeResult;
    @SerializedName("CountRegenerate")
    private String countRegenerate;
    @SerializedName("SMSID")
    private String smsId;
}
