package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.Status;

@Entity
@Table(name = "dinner_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DinnerTableModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tableName;
    
    private Boolean enoughtFood = false;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_ACTIVE; 
    
    @ToString.Exclude
    @OneToMany(mappedBy = "dinnerTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderModel> orders = new ArrayList<>();
}
