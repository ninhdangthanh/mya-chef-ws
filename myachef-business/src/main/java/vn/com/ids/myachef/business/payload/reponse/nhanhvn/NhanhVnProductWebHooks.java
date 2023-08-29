package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhanhVnProductWebHooks extends AbstractNhanhVnWebHooksResponse {
    private NhanhVnWebHooksData data;
}
