package vn.com.ids.myachef.business.zalo.article.dto.normal;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrapperNormalArticle {

    private int error;

    private String message;

    @SerializedName("data")
    private NormalArticle article;

}
