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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.BannerType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_upload")
@EntityListeners(AuditingEntityListener.class)
public class FileUploadModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "generated_name")
    private String generatedName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "size")
    private Long size = 0L;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "thumbnail_size")
    private Long thumbnailSize = 0L;

    @Column(name = "is_banner", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isBanner;

    @Column(name = "bannerType")
    @Enumerated(EnumType.STRING)
    private BannerType bannerType;
}
