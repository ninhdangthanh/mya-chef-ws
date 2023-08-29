package vn.com.ids.myachef.business.zalo.article.dto.normal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ids.myachef.dao.enums.zalo.article.ZaloBodyType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Body {

    @SerializedName("type")
    private ZaloBodyType type;

    // applied text only
    @SerializedName("content")
    private String content;

    // applied image only
    @SerializedName("caption")
    private String caption;

    // applied image and video
    @SerializedName("url")
    private String url;

    // applied video only
    @Expose(serialize = true)
    @SerializedName("video_id")
    private String videoId;

    @SerializedName("thumb")
    private String thumbnailUrl;

    // applied product only
    @SerializedName("id")
    private String productId;
}