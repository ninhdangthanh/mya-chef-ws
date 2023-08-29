package vn.com.ids.myachef.business.zalo.broadcast.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastArticleRequest {

    @SerializedName("recipient")
    private BroadcastRecipient recipient;

    @SerializedName("message")
    private BroadcastMessage message;
}
