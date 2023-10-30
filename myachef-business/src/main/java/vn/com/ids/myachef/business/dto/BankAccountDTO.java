package vn.com.ids.myachef.business.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class BankAccountDTO {

    private Long id;

    @NotNull(message = "Field 'bankAccountNumber': {notnull}", groups = { OnCreate.class })
    private Long bankAccountNumber;

    @NotBlank(message = "Field 'bankAccountName': {notblank}", groups = { OnCreate.class })
    private String bankAccountName;

    private String bankName;

    private Status status;
}
