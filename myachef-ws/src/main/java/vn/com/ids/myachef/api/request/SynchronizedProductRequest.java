package vn.com.ids.myachef.api.request;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SynchronizedProductRequest{
    @Min(value = 1, message = "Page must be greater than zero")
    private int page;
    @Min(value = 1, message = "Page size must be greater than zero")
    private int icpp;
    
    @Override
    public String toString() {
        return String.format("{'page' : %s , 'icpp' : %s}", page, icpp);
    }
}