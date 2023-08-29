package vn.com.ids.myachef.business.service.nhanhvn.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NhanhVNLocationResponse extends NhanhVNAbstractResponse {

    private List<DataItem> data = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class DataItem {
        private int id;

        private int parentId;

        private int cityId;
        private int cityLocationId;

        private int districtId;
        private int districtLocationId;

        private String name;
    }

}
