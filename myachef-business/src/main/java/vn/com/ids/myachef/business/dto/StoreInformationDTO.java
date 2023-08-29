package vn.com.ids.myachef.business.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreInformationDTO {
   
    private Long id;
    
    private String introduction;

    private String paymentMethod;
    
    private String privacyPolicy;

    private String returnPolicy;

    private String deliveryPolicy;

    private String inspectionPolicy;

    private String responsibility;

    private String disclaimer;

    private Boolean isDefault;
}
