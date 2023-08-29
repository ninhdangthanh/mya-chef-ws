package vn.com.ids.myachef.business.zalo.article.media;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("status")
    private String status;

    @SerializedName("total_view")
    private int totalView = 0;

    @SerializedName("total_share")
    private int totalShare = 0;

    @SerializedName("create_date")
    private long createDate;

    @SerializedName("update_date")
    private long updateDate;

    @SerializedName("thumb")
    private String thumbnailUrl;

    @SerializedName("link_view")
    private String linkView;
}
