package vn.com.ids.myachef.dao.criteria;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.OnlinePaymentType;
import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.enums.SubscriptionCustomerType;

@Getter
@Setter
public class SubscriptionCustomerDetailCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    // Default pattern is "yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    // Default pattern is "yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

    private PaymentStatus paymentStatus;

    private OnlinePaymentType paymentType;

    private SubscriptionCustomerType type;

    private Long subscriptionId;

    private Long customerId;

    private Boolean isGetFullObjectData = false;
}
