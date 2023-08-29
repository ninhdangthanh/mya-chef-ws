package vn.com.ids.myachef.dao.criteria;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.UserRole;
import vn.com.ids.myachef.dao.enums.UserStatus;

@Getter
@Setter
public class UserCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    private String searchText;

    private UserRole role;

    private UserStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;
}
