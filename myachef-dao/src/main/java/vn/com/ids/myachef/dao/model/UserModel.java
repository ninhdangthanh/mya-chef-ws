package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.time.LocalDate;
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
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ids.myachef.dao.enums.UserRole;
import vn.com.ids.myachef.dao.enums.UserStatus;

@Entity
@Table(name = "`users`")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @Size(max = 255, message = "The max size of username is 255 characters.")
    private String username;

    @Column(name = "password")
    @Size(min = 6, max = 255, message = "The max size of password is 255 characters.")
    private String password;

    @Column(name = "phone_number")
    @Size(max = 50, message = "The max size of Name is 50 characters.")
    private String phoneNumber;

    @Column(name = "full_name")
    @Size(max = 100, message = "The max size of fullName is 100 characters.")
    private String fullName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "avatar_file")
    @Size(max = 255, message = "The max size of avatarFile is 255 characters.")
    private String avatarFile;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "address")
    @Size(max = 255, message = "The max size of address is 255 characters.")
    private String address;

    @Column(name = "search_text")
    @Size(max = 1000, message = "The max size of searchText is 1000 characters.")
    private String searchText;

    @Column(name = "zalo_access_token", columnDefinition = "TEXT")
    private String zaloAccessToken;

}
