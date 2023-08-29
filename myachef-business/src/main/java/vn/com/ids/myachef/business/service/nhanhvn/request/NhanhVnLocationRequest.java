package vn.com.ids.myachef.business.service.nhanhvn.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NhanhVnLocationRequest {

    @NotNull
    private LocationType type;

    private int parentId;

    public enum LocationType {
        CITY, DISTRICT, WARD
    }
}
