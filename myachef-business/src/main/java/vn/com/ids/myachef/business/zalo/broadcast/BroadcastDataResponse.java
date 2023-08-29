package vn.com.ids.myachef.business.zalo.broadcast;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastDataResponse {
    @SerializedName("message_id")
    public String messageId;
    
    @SerializedName("user_id")
    public String userId;

}
