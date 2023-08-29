package vn.com.ids.myachef.business.service.impl;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.converter.FrequentlyAskedQuestionConverter;
import vn.com.ids.myachef.business.dto.FrequentlyAskedQuestionDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.FrequentlyAskedQuestionService;
import vn.com.ids.myachef.dao.criteria.FrequentlyAskedQuestionCriteria;
import vn.com.ids.myachef.dao.criteria.builder.FrequentlyAskedQuestionSpecificationBuilder;
import vn.com.ids.myachef.dao.model.FrequentlyAskedQuestionModel;
import vn.com.ids.myachef.dao.repository.FrequentlyAskedQuestionRepository;

@Service
@Transactional
public class FrequentlyAskedQuestionServiceImpl extends AbstractService<FrequentlyAskedQuestionModel, Long> implements FrequentlyAskedQuestionService {

    private FrequentlyAskedQuestionRepository frequentlyAskedQuestionRepository;

    private FrequentlyAskedQuestionConverter frequentlyAskedQuestionConverter;

    protected FrequentlyAskedQuestionServiceImpl(FrequentlyAskedQuestionRepository frequentlyAskedQuestionRepository) {
        super(frequentlyAskedQuestionRepository);
        this.frequentlyAskedQuestionRepository = frequentlyAskedQuestionRepository;
    }

    public Specification<FrequentlyAskedQuestionModel> buildSpecification(FrequentlyAskedQuestionCriteria frequentlyAskedQuestionCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new FrequentlyAskedQuestionSpecificationBuilder(root, criteriaBuilder) //
                .setTimeFrame(frequentlyAskedQuestionCriteria.getFrom(), frequentlyAskedQuestionCriteria.getTo())//
                .setTitle(frequentlyAskedQuestionCriteria.getTitle())//
                .build();
    }

    @Override
    public Page<FrequentlyAskedQuestionModel> findAll(FrequentlyAskedQuestionCriteria frequentlyAskedQuestionCriteria) {
        Specification<FrequentlyAskedQuestionModel> specification = buildSpecification(frequentlyAskedQuestionCriteria);
        Pageable pageable = buildPageable(frequentlyAskedQuestionCriteria);
        return frequentlyAskedQuestionRepository.findAll(specification, pageable);
    }

    @Override
    public FrequentlyAskedQuestionDTO create(@Valid FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
        FrequentlyAskedQuestionModel frequentlyAskedQuestionModel = frequentlyAskedQuestionConverter.toModel(frequentlyAskedQuestionDTO);
        frequentlyAskedQuestionModel = save(frequentlyAskedQuestionModel);
        return frequentlyAskedQuestionConverter.toDTO(frequentlyAskedQuestionModel);
    }

    @Override
    public FrequentlyAskedQuestionDTO update(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
        FrequentlyAskedQuestionModel frequentlyAskedQuestionModel = findOne(frequentlyAskedQuestionDTO.getId());
        if (frequentlyAskedQuestionModel == null) {
            throw new ResourceNotFoundException("Not found frequentlyAskedQuestion with id: " + frequentlyAskedQuestionDTO.getId());
        }
        frequentlyAskedQuestionConverter.mapDataForUpdate(frequentlyAskedQuestionModel, frequentlyAskedQuestionDTO);
        frequentlyAskedQuestionModel = save(frequentlyAskedQuestionModel);

        return frequentlyAskedQuestionConverter.toDTO(frequentlyAskedQuestionModel);
    }

}
