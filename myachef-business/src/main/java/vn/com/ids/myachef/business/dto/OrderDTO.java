package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.OrderStatus;

@Getter
@Setter
public class OrderDTO {
    private Long id;

    private Double totalPayment;

    private String imagePayment;

    private Boolean isPaymentWithBanking = false;
    
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private DinnerTableDTO dinnerTableDTO;

    private Long dinnerTableId;

    private Long userId;

    private OrderStatus status;

    private UserDTO userDTO;

    private List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();

    private List<Long> dishIds = new ArrayList<>();
}
