package vn.com.ids.myachef.business.zalo.broadcast;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastArticleResponse {

    public int error;
    public String message;

    @SerializedName("data")
    public BroadcastDataResponse data;

}
