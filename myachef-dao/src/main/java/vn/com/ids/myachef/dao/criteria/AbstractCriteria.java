package vn.com.ids.myachef.dao.criteria;

import java.io.Serializable;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AbstractCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Integer DEFAULT_PAGE_INDEX = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 10;

//    @Schema(defaultValue = "0")
    @Size(min = 0)
    private int pageIndex = DEFAULT_PAGE_INDEX;

//    @Schema(defaultValue = "10")
    @Size(min = 5, max = 100)
    private int pageSize = DEFAULT_PAGE_SIZE;

    private String sortBy;

//    @Schema(defaultValue = "true")
    private Boolean ascending;

}
