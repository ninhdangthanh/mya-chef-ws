package vn.com.ids.myachef.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)

@TypeDef(name = "json", typeClass = JsonStringType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class StoreInformationModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Type(type = "json")
    @Column(name = "introduction", columnDefinition = "MEDIUMTEXT")
    private String introduction;
    
    @Type(type = "json")
    @Column(name = "payment_method", columnDefinition = "MEDIUMTEXT")
    private String paymentMethod;
    
    @Type(type = "json")
    @Column(name = "privacy_policy", columnDefinition = "MEDIUMTEXT")
    private String privacyPolicy;
    
    @Type(type = "json")
    @Column(name = "return_policy", columnDefinition = "MEDIUMTEXT")
    private String returnPolicy;
    
    @Type(type = "json")
    @Column(name = "delivery_policy", columnDefinition = "MEDIUMTEXT")
    private String deliveryPolicy;
    
    @Type(type = "json")
    @Column(name = "inspection_policy", columnDefinition = "MEDIUMTEXT")
    private String inspectionPolicy;
    
    @Type(type = "json")
    @Column(name = "responsibility", columnDefinition = "MEDIUMTEXT")
    private String responsibility;
    
    @Type(type = "json")
    @Column(name = "disclaimer", columnDefinition = "MEDIUMTEXT")
    private String disclaimer;
    
    @Column(name = "is_default", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDefault;

}
