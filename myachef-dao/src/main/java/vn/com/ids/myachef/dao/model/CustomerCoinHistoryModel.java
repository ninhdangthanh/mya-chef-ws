package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryScope;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryType;

@Entity
@Table(name = "`customer_coin_history`")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CustomerCoinHistoryModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CustomerCoinHistoryType type;

    @Column(name = "scope")
    @Enumerated(EnumType.STRING)
    private CustomerCoinHistoryScope scope;

    private Long quantity;

    private Long customerId;

    private String customerFullName;

    private Long scopeId;
}
