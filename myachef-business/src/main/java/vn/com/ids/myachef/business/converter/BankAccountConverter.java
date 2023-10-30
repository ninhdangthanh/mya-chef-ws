package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.dto.BankAccountDTO;
import vn.com.ids.myachef.dao.model.BankAccountModel;

@Component
public class BankAccountConverter {

    @Autowired
    private ModelMapper mapper;

    public List<BankAccountDTO> toBasicDTOs(List<BankAccountModel> bankAccountModels) {
        List<BankAccountDTO> bankAccountDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(bankAccountModels)) {
            for (BankAccountModel bankAccountModel : bankAccountModels) {
                bankAccountDTOs.add(toBasicDTO(bankAccountModel));
            }
        }
        return bankAccountDTOs;
    }

    public BankAccountDTO toBasicDTO(BankAccountModel bankAccountModel) {
        return mapper.map(bankAccountModel, BankAccountDTO.class);
    }

    public BankAccountModel toModel(BankAccountDTO bankAccountDTO) {
        return mapper.map(bankAccountDTO, BankAccountModel.class);
    }

    public void update(BankAccountModel bankAccountModel, BankAccountDTO bankAccountDTO) {
        if (bankAccountDTO.getBankAccountNumber() != null) {
            bankAccountModel.setBankAccountNumber(bankAccountDTO.getBankAccountNumber());
        }

        if (StringUtils.hasText(bankAccountDTO.getBankAccountName())) {
            bankAccountModel.setBankAccountName(bankAccountDTO.getBankAccountName());
        }

        if (StringUtils.hasText(bankAccountDTO.getBankName())) {
            bankAccountModel.setBankName(bankAccountDTO.getBankName());
        }

        if (bankAccountDTO.getStatus() != null) {
            bankAccountModel.setStatus(bankAccountDTO.getStatus());
        }
    }
}
