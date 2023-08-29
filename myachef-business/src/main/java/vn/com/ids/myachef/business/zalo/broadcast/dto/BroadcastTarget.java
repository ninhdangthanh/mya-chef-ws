package vn.com.ids.myachef.business.zalo.broadcast.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastTarget {

    @SerializedName("ages")
    private String ages;

    @SerializedName("gender")
    private String gender;

    @SerializedName("locations")
    private String locations;

    @SerializedName("cities")
    private String cities;

    @SerializedName("platform")
    private String platform;

}
