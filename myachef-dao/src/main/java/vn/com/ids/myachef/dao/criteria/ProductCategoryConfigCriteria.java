package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class ProductCategoryConfigCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    private String name;

    private String nhanhVnId;

    private Status status;
}
