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
@AllArgsConstructor
public abstract class NhanhVNAbstractResponse {

    private int code;
    private List<Object> messages = new ArrayList<>();

}
