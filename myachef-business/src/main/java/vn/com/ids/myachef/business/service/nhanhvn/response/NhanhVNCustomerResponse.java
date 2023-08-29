package vn.com.ids.myachef.business.service.nhanhvn.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NhanhVNCustomerResponse extends NhanhVNAbstractResponse {

    private List<DataItem> data = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class DataItem {
        @SerializedName(value = "messege")
        private String message;
        private String id;
    }

}
