package vn.com.ids.myachef.business.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.FrequentlyAskedQuestionDTO;
import vn.com.ids.myachef.dao.criteria.FrequentlyAskedQuestionCriteria;
import vn.com.ids.myachef.dao.model.FrequentlyAskedQuestionModel;

public interface FrequentlyAskedQuestionService extends IGenericService<FrequentlyAskedQuestionModel, Long> {

    public Page<FrequentlyAskedQuestionModel> findAll(FrequentlyAskedQuestionCriteria frequentlyAskedQuestionCriteria);

    public FrequentlyAskedQuestionDTO create(@Valid FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO);

    public FrequentlyAskedQuestionDTO update(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO);
}
