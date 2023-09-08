package vn.com.ids.myachef.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.OrderDetailStatus;

@Entity
@Table(name = "order_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderDetailModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OrderDetailStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	private OrderModel order;

	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	private DishModel dish;
}
