package vn.com.ids.myachef.business.payload.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    public String provider;
    public String latitude;
    public String longitude;
    public String timestamp;
}