package vn.com.ids.myachef.business.zalo.article.media;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaData {

    @SerializedName("medias")
    private List<Media> medias = new ArrayList<Media>();

    @SerializedName("total")
    private int total = 0;

}
