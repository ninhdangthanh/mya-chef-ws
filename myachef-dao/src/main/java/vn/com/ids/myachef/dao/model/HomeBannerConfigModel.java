package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.HomeBannerConfigType;
import vn.com.ids.myachef.dao.enums.HomeConfigBannerPosition;

@Entity
@Table(name = "home_banner_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeBannerConfigModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bannerFileName;

    private String name;

    @Column(name = "`order`")
    private int order;

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private HomeConfigBannerPosition position;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private HomeBannerConfigType type;

    private Long typeId;

    @Type(type = "json")
    @Column(name = "banner_config_products", columnDefinition = "LONGTEXT")
    private List<HomeCategoryProductConfig> bannerConfigProducts;

    private boolean isInterstitialAd = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private HomeConfigModel homeConfig;
}
