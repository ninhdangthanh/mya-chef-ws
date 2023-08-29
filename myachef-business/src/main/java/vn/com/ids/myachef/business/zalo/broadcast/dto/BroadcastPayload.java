package vn.com.ids.myachef.business.zalo.broadcast.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastPayload {

    @SerializedName("template_type")
    private String templateType;

    @SerializedName("elements")
    private List<BroadcastElement> elements;

}
