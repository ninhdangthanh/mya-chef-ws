package vn.com.ids.myachef.business.zalo.article.media;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrapperMedia {

    private int error;

    private String message;

    @SerializedName("data")
    private MediaData mediaData;

}
