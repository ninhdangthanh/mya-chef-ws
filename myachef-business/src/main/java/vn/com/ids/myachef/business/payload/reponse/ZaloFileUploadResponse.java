package vn.com.ids.myachef.business.payload.reponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZaloFileUploadResponse {
    private int error;
    private String message;
    private List<String> urls;
    private String imageName;
}
