package vn.com.ids.myachef.business.zalo.article.response.video;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrapperArticleVideoResponse {

    private int error;

    private String message;

    @SerializedName("data")
    private ArticleVideoResponse articleVideo;

}
