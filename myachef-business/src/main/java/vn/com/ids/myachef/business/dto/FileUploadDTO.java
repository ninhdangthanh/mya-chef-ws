package vn.com.ids.myachef.business.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private Date createdDate;

    private String generatedName;

    private String mimeType;

    private String originalName;

    private Long size = 0L;

    private String thumbnail;

    private Long thumbnailSize = 0L;

    // additional attribute
    private String path;

    // additional attribute
    private String thumbnailPath;

}