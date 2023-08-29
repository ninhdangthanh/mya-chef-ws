package vn.com.ids.myachef.business.zalo.article.dto.normal;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ids.myachef.dao.enums.zalo.article.ZaloCoverStatus;
import vn.com.ids.myachef.dao.enums.zalo.article.ZaloCoverType;
import vn.com.ids.myachef.dao.enums.zalo.article.ZaloCoverView;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cover {

    @SerializedName("cover_type")
    private ZaloCoverType coverType;

    @SerializedName("status")
    private ZaloCoverStatus status;

    // applied image only
    @SerializedName("photo_url")
    private String photoUrl;

    // applied video only
    @SerializedName("cover_view")
    private ZaloCoverView coverView;

    @SerializedName("video_id")
    private String videoId;

}