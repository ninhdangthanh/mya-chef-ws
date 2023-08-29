package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class ProductConfigCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    private String name;

    private String nhanhVnId;

    private String nhanhVnCategoryId;

    private Status status;

    private Boolean isNew;

    private Long ignoreRelatedProductNhanhVnId;
}
