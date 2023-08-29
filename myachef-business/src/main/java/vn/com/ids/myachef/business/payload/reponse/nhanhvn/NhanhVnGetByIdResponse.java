package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnGetByIdResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private String data;

    @SerializedName("message")
    private String message;
}
