package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class SalePackageCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    private String name;

    private Status status;
}
