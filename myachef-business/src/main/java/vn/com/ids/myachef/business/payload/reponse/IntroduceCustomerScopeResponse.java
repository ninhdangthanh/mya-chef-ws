package vn.com.ids.myachef.business.payload.reponse;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.dto.CustomerAffiliateDetailDTO;
import vn.com.ids.myachef.business.dto.SaleDTO;

@Getter
@Setter
@AllArgsConstructor
public class IntroduceCustomerScopeResponse {

    private SaleDTO saleDTO;

    private List<CustomerAffiliateDetailDTO> customerAffiliateDetails = new ArrayList<>();
}
