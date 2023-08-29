package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.dto.NhanhVnDTO;
import vn.com.ids.myachef.dao.model.NhanhVnModel;

@Component
public class NhanhVnConverter {

    @Autowired
    private ModelMapper mapper;

    public List<NhanhVnDTO> toBasicDTOs(List<NhanhVnModel> models) {
        List<NhanhVnDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (NhanhVnModel nhanhVnModel : models) {
                dtos.add(toBasicDTO(nhanhVnModel));
            }
        }
        return dtos;
    }

    public NhanhVnDTO toBasicDTO(NhanhVnModel model) {
        NhanhVnDTO nhanhVnDTO = mapper.map(model, NhanhVnDTO.class);
        nhanhVnDTO.setSecretKey(null);
        return nhanhVnDTO;
    }

    public NhanhVnModel toModel(NhanhVnDTO nhanhVnDTO) {
        return mapper.map(nhanhVnDTO, NhanhVnModel.class);
    }

    public void update(NhanhVnModel nhanhVnModel, NhanhVnDTO nhanhVnDTO) {
        boolean resetAccount = false;
        if (StringUtils.hasText(nhanhVnDTO.getAppId())) {
            nhanhVnModel.setAppId(nhanhVnDTO.getAppId());
            resetAccount = true;
        }

        if (StringUtils.hasText(nhanhVnDTO.getVersion())) {
            nhanhVnModel.setVersion(nhanhVnDTO.getVersion());
            resetAccount = true;
        }

        if (nhanhVnDTO.getPriority() != null) {
            nhanhVnModel.setPriority(nhanhVnDTO.getPriority());
        }

        if (nhanhVnDTO.getStatus() != null) {
            nhanhVnModel.setStatus(nhanhVnDTO.getStatus());
        }

        if (StringUtils.hasText(nhanhVnDTO.getSecretKey())) {
            nhanhVnModel.setSecretKey(nhanhVnDTO.getSecretKey());
            resetAccount = true;
        }

        if (resetAccount) {
            nhanhVnModel.setAccessToken(null);
            nhanhVnModel.setBusinessId(null);
            nhanhVnModel.setExpiredDate(null);
        }
    }
}
