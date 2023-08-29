package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    private String name;

    private String tagLine;
}
