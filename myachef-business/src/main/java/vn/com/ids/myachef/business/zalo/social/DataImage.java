package vn.com.ids.myachef.business.zalo.social;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataImage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String url;
}

