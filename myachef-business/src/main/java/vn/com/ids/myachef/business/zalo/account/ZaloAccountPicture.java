package vn.com.ids.myachef.business.zalo.account;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ZaloAccountPicture {
    @SerializedName("data")
    private ZaloAccountPictureData data;
    
}
