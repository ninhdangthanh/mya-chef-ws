package vn.com.ids.myachef.business.zalo.article.response.video;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVideoResponse {

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("video_name")
    private String videoName;

    @SerializedName("video_size")
    private long videoSize = 0L;

    @SerializedName("convert_percent")
    private int convertPercent = 0;

    @SerializedName("convert_error_code")
    private int convertErrorCode;

    @SerializedName("video_id")
    private String videoId;

    @SerializedName("status")
    private int status;

}
