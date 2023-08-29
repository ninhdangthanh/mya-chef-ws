package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.BankAccountConverter;
import vn.com.ids.myachef.business.dto.BankAccountDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.BankAccountService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.BankAccountModel;

@RestController
@RequestMapping("/api/bank-account")
@Slf4j
@Validated
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountConverter bankAccountConverter;

    @Operation(summary = "Find all by status")
    @GetMapping("/status")
    public List<BankAccountDTO> findAllByStatus(@RequestParam Status status) {
        log.info("------------------ Find all by status - START ----------------");
        return bankAccountService.findAllByStatus(status);
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public BankAccountDTO findById(@PathVariable Long id) {
        log.info("------------------ Find by id - START ----------------");
        BankAccountModel bankAccountModel = bankAccountService.findOne(id);
        if (bankAccountModel == null) {
            throw new ResourceNotFoundException("Not found bank account with id: " + id);
        }
        log.info("------------------ Find by id - END ----------------");
        return bankAccountConverter.toBasicDTO(bankAccountModel);
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public BankAccountDTO create(@RequestBody @Valid BankAccountDTO bankAccountDTO) {
        log.info("------------------ Create - START ----------------");
        return bankAccountService.create(bankAccountDTO);
    }

    @Operation(summary = "Update")
    @PatchMapping("/{id}")
    public BankAccountDTO update(@PathVariable Long id, @RequestBody BankAccountDTO bankAccountDTO) {
        log.info("------------------ Update - START ----------------");
        BankAccountModel bankAccountModel = bankAccountService.findOne(id);
        if (bankAccountModel == null) {
            throw new ResourceNotFoundException("Not found bank account with id: " + id);
        }
        return bankAccountService.update(bankAccountModel, bankAccountDTO);
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);
        if (!CollectionUtils.isEmpty(ids)) {
            bankAccountService.deleteByIds(ids);
        }

        log.info("------------------ Delete - END ----------------");
    }
}
