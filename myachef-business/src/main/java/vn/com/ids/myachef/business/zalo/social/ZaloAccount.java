package vn.com.ids.myachef.business.zalo.social;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.com.ids.myachef.business.zalo.account.ZaloAccountPicture;

@Getter
@Setter
@ToString
public class ZaloAccount {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;
    
    @SerializedName("birthday")
    private String birthday;

    @SerializedName("error")
    private long error;

    @SerializedName("message")
    private String message;

    @SerializedName("picture")
    private ZaloAccountPicture picture;
}
