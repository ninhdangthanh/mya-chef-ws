package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.dto.FrequentlyAskedQuestionDTO;
import vn.com.ids.myachef.dao.model.FrequentlyAskedQuestionModel;

@Component
public class FrequentlyAskedQuestionConverter {

    @Autowired
    private ModelMapper mapper;

    public FrequentlyAskedQuestionDTO toBasicDTO(FrequentlyAskedQuestionModel frequentlyAskedQuestionModel) {
        return mapper.map(frequentlyAskedQuestionModel, FrequentlyAskedQuestionDTO.class);
    }

    public List<FrequentlyAskedQuestionDTO> toBasicDTOs(List<FrequentlyAskedQuestionModel> frequentlyAskedQuestionModels) {
        if (CollectionUtils.isEmpty(frequentlyAskedQuestionModels)) {
            return new ArrayList<>();
        }

        List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOs = new ArrayList<>();
        for (FrequentlyAskedQuestionModel frequentlyAskedQuestionModel : frequentlyAskedQuestionModels) {
            frequentlyAskedQuestionDTOs.add(toBasicDTO(frequentlyAskedQuestionModel));
        }

        return frequentlyAskedQuestionDTOs;
    }

    public FrequentlyAskedQuestionDTO toDTO(FrequentlyAskedQuestionModel frequentlyAskedQuestionModel) {
        return toBasicDTO(frequentlyAskedQuestionModel);
    }

    public List<FrequentlyAskedQuestionDTO> toDTOs(List<FrequentlyAskedQuestionModel> frequentlyAskedQuestionModels) {
        if (CollectionUtils.isEmpty(frequentlyAskedQuestionModels)) {
            return new ArrayList<>();
        }

        List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOs = new ArrayList<>();
        for (FrequentlyAskedQuestionModel frequentlyAskedQuestionModel : frequentlyAskedQuestionModels) {
            frequentlyAskedQuestionDTOs.add(toDTO(frequentlyAskedQuestionModel));
        }

        return frequentlyAskedQuestionDTOs;
    }

    public FrequentlyAskedQuestionModel toModel(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
        FrequentlyAskedQuestionModel frequentlyAskedQuestionModel = mapper.map(frequentlyAskedQuestionDTO, FrequentlyAskedQuestionModel.class);
        return frequentlyAskedQuestionModel;
    }

    public void mapDataForUpdate(FrequentlyAskedQuestionModel frequentlyAskedQuestionModel, FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
        if (frequentlyAskedQuestionDTO.getTitle() != null) {
            frequentlyAskedQuestionModel.setTitle(frequentlyAskedQuestionDTO.getTitle());
        }
        if (frequentlyAskedQuestionDTO.getDescription() != null) {
            frequentlyAskedQuestionModel.setDescription(frequentlyAskedQuestionDTO.getDescription());
        }
    }

}
