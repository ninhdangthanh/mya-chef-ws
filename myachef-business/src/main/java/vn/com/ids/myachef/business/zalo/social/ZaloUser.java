package vn.com.ids.myachef.business.zalo.social;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ZaloUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String id;
    private int error;
    private String message;
    @SerializedName("picture")
    private Picture picture;
}
