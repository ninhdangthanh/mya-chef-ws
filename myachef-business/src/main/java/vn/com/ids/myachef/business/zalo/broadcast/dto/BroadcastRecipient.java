package vn.com.ids.myachef.business.zalo.broadcast.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastRecipient {

    @SerializedName("target")
    public BroadcastTarget target;

}
