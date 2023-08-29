package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreInformationCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;
    
    private Boolean isDefault;

}
