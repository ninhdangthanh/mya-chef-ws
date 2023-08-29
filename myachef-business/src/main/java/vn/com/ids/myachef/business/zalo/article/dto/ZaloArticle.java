package vn.com.ids.myachef.business.zalo.article.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ids.myachef.dao.enums.zalo.article.ZaloArticleStatus;
import vn.com.ids.myachef.dao.enums.zalo.article.ZaloArticleType;
import vn.com.ids.myachef.dao.enums.zalo.article.ZaloEnableComment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZaloArticle {

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private ZaloArticleType type;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private ZaloArticleStatus status;

    @SerializedName("comment")
    private ZaloEnableComment comment;

}
