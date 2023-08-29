package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AbstractOrder {
    private String orderBy;
    private boolean ascending;
}
