package vn.com.ids.myachef.business.zalo.article.dto.normal;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.ids.myachef.business.zalo.article.dto.ZaloArticle;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class NormalArticle extends ZaloArticle {

    @SerializedName("author")
    private String author;

    @SerializedName("cover")
    private Cover cover;

    @SerializedName("body")
    private List<Body> body;

    @SerializedName("related_medias")
    private List<String> relatedMedias;

    @SerializedName("tracking_link")
    private String trackingLink;

    @SerializedName("cite")
    private Cite cite;
}
