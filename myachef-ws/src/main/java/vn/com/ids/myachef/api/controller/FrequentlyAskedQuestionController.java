package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
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
import vn.com.ids.myachef.business.converter.FrequentlyAskedQuestionConverter;
import vn.com.ids.myachef.business.dto.FrequentlyAskedQuestionDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.FrequentlyAskedQuestionService;
import vn.com.ids.myachef.dao.criteria.FrequentlyAskedQuestionCriteria;
import vn.com.ids.myachef.dao.model.FrequentlyAskedQuestionModel;

@RestController
@RequestMapping("/api/frequently-asked-questions")
@Slf4j
public class FrequentlyAskedQuestionController {

    @Autowired
    private FrequentlyAskedQuestionService frequentlyAskedQuestionService;

    @Autowired
    private FrequentlyAskedQuestionConverter frequentlyAskedQuestionConverter;

    @Operation(summary = "Find by criteria")
    @GetMapping("/search")
    public Page<FrequentlyAskedQuestionDTO> findBycriteria(@ParameterObject FrequentlyAskedQuestionCriteria frequentlyAskedQuestionCriteria) {
        log.info("------------------ FrequentlyAskedQuestionController - Find by criteria - START ----------------");
        Page<FrequentlyAskedQuestionModel> page = frequentlyAskedQuestionService.findAll(frequentlyAskedQuestionCriteria);
        List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOs = frequentlyAskedQuestionConverter.toDTOs(page.getContent());
        Pageable pageable = PageRequest.of(frequentlyAskedQuestionCriteria.getPageIndex(), frequentlyAskedQuestionCriteria.getPageSize());
        log.info("------------------ FrequentlyAskedQuestionController - Find by criteria - END ----------------");
        return new PageImpl<>(frequentlyAskedQuestionDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public FrequentlyAskedQuestionDTO findbyId(@PathVariable("id") Long id) {
        log.info("------------------ FrequentlyAskedQuestionController - findById - START ----------------");
        FrequentlyAskedQuestionModel frequentlyAskedQuestionModel = frequentlyAskedQuestionService.findOne(id);
        if (frequentlyAskedQuestionModel == null) {
            throw new ResourceNotFoundException("Not found frequentlyAskedQuestion with id: " + id);
        }
        log.info("------------------ FrequentlyAskedQuestionController - findById - END ----------------");
        return frequentlyAskedQuestionConverter.toDTO(frequentlyAskedQuestionModel);
    }

    @Operation(summary = "Create")
    @PostMapping
    public FrequentlyAskedQuestionDTO create(@Valid @RequestBody FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
        log.info("------------------ FrequentlyAskedQuestionController - Create - START ----------------");
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionResponse = frequentlyAskedQuestionService.create(frequentlyAskedQuestionDTO);
        log.info("------------------ FrequentlyAskedQuestionController - Create - END ----------------");
        return frequentlyAskedQuestionResponse;
    }

    @Operation(summary = "Update")
    @PatchMapping
    public FrequentlyAskedQuestionDTO update(@RequestBody FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
        log.info("------------------ FrequentlyAskedQuestionController - Update - START ----------------");
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionResponse = frequentlyAskedQuestionService.update(frequentlyAskedQuestionDTO);
        log.info("------------------ FrequentlyAskedQuestionController - Update - END ----------------");
        return frequentlyAskedQuestionResponse;
    }

    @Operation(summary = "Delete")
    @DeleteMapping()
    public void delete(@RequestParam List<Long> ids) {
        log.info("------------------ FrequentlyAskedQuestionController - Delete - START ----------------");
        if (!CollectionUtils.isEmpty(ids)) {
            frequentlyAskedQuestionService.deleteByIds(ids);
        }
        log.info("------------------ FrequentlyAskedQuestionController - Delete - END ----------------");
    }

}
