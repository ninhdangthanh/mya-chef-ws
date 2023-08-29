package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrequentlyAskedQuestionDTO {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
