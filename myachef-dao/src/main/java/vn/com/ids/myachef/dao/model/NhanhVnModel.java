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
import vn.com.ids.myachef.dao.enums.Status;

@Entity
@Table(name = "nhanh_vn")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhanhVnModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "business_id")
    private String businessId;

    @Column(name = "version")
    private String version;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

    @Column(name = "priority")
    private int priority;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
