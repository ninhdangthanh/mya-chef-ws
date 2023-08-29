package vn.com.ids.myachef.dao.criteria;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FrequentlyAskedQuestionCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    // Default pattern is "yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    // Default pattern is "yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

    private String title;

}
