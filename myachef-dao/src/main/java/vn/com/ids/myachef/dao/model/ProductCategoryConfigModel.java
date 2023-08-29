package vn.com.ids.myachef.dao.model;

import java.io.Serializable;

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
import vn.com.ids.myachef.dao.enums.Status;

@Entity
@Table(name = "product_category_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryConfigModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    private String nhanhVnId;

    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private String banner;
}
