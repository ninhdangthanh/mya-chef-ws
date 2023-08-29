package vn.com.ids.myachef.dao.criteria;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.IntroduceCustomerScope;
import vn.com.ids.myachef.dao.enums.PromotionType;
import vn.com.ids.myachef.dao.enums.SaleScope;
import vn.com.ids.myachef.dao.enums.SaleType;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class SaleCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    // Default pattern is "yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    // Default pattern is "yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

    private String name;

    private String code;

    private Status status;

    private SaleType type;

    private SaleScope saleScope;

    private PromotionType promotionType;

    private Boolean isIntroduceCustomer;

    private IntroduceCustomerScope introduceCustomerScope;
}
