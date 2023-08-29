package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeConfigDTO {

    private Long id;

    private String dailyQuestion;

    private String notification;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private List<HomeBannerConfigDTO> bannerSliders = new ArrayList<>();

    private List<HomeBannerConfigDTO> bodies = new ArrayList<>();
}
