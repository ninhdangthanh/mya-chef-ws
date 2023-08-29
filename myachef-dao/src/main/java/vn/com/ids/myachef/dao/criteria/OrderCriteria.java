package vn.com.ids.myachef.dao.criteria;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.OrderPaymentMethod;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.OrderType;

@Getter
@Setter
@NoArgsConstructor
public class OrderCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    // Default pattern is "yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    // Default pattern is "yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

    private Double totalMoneyFrom;
    private Double totalMoneyTo;

    private List<OrderStatus> statuses;
    
    private OrderType type;
    private OrderPaymentMethod paymentMethod;
    
    private List<Long> customerIds;

}
