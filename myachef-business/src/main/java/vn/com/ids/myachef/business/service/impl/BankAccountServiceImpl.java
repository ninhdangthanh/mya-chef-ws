package vn.com.ids.myachef.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.BankAccountConverter;
import vn.com.ids.myachef.business.dto.BankAccountDTO;
import vn.com.ids.myachef.business.service.BankAccountService;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.BankAccountModel;
import vn.com.ids.myachef.dao.repository.BankAccountRepository;

@Service
@Transactional
@Slf4j
public class BankAccountServiceImpl extends AbstractService<BankAccountModel, Long> implements BankAccountService {

    private BankAccountRepository bankAccountRepository;

    protected BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        super(bankAccountRepository);
        this.bankAccountRepository = bankAccountRepository;
    }

    @Autowired
    private BankAccountConverter bankAccountConverter;

    @Override
    public List<BankAccountDTO> findAllByStatus(Status status) {
        List<BankAccountModel> bankAccountModels = null;
        if (status != null) {
            bankAccountModels = bankAccountRepository.findAll();
        } else {
            bankAccountModels = bankAccountRepository.findByStatus(Status.ACTIVE);
        }

        log.info("------------------ Find all by status - END ----------------");
        return bankAccountConverter.toBasicDTOs(bankAccountModels);
    }

    @Override
    public BankAccountDTO create(BankAccountDTO bankAccountDTO) {
        BankAccountModel bankAccountModel = bankAccountConverter.toModel(bankAccountDTO);
        bankAccountModel.setStatus(Status.ACTIVE);
        bankAccountRepository.save(bankAccountModel);
        log.info("------------------ Create - END ----------------");
        return bankAccountConverter.toBasicDTO(bankAccountModel);
    }

    @Override
    public BankAccountDTO update(BankAccountModel bankAccountModel, BankAccountDTO bankAccountDTO) {
        bankAccountConverter.update(bankAccountModel, bankAccountDTO);
        bankAccountRepository.save(bankAccountModel);
        log.info("------------------ Update - END ----------------");
        return bankAccountConverter.toBasicDTO(bankAccountModel);
    }
}
