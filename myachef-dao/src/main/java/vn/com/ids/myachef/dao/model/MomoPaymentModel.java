package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ids.myachef.dao.enums.MomoPaymentStatus;

@Entity
@Table(name = "momo_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MomoPaymentModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partnerCode;

    private String orderId;

    private Double amount;

    private String responseTime;

    private String message;

    private int resultCode;

    private String payUrl;

    private String deeplink;

    private String qrCodeUrl;

    private String requestId;

    private String transId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MomoPaymentStatus status;

    private LocalDateTime successDate;
}
