package vn.com.ids.myachef.business.zalo.social;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Picture implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("data")
    private DataImage data;
}
