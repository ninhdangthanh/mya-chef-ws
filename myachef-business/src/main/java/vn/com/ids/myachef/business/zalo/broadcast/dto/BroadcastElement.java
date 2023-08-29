package vn.com.ids.myachef.business.zalo.broadcast.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastElement {

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("attachment_id")
    private String attachmentId;

}
