package vn.com.ids.myachef.business.zalo.article.dto.video;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.ids.myachef.business.zalo.article.dto.ZaloArticle;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class VideoArticle extends ZaloArticle {

    @SerializedName("video_id")
    private String videoId;

    @SerializedName("avatar")
    private String avatar;
}
