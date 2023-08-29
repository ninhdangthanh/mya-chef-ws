package vn.com.ids.myachef.business.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.converter.NhanhVnConverter;
import vn.com.ids.myachef.business.dto.NhanhVnDTO;
import vn.com.ids.myachef.business.service.NhanhVnService;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.NhanhVnModel;
import vn.com.ids.myachef.dao.repository.NhanhVnRepository;

@Transactional
@Service
public class NhanhVnServiceImpl extends AbstractService<NhanhVnModel, Long> implements NhanhVnService {

    private NhanhVnRepository nhanhVnRepository;

    @Autowired
    private NhanhVnConverter nhanhVnConverter;

    protected NhanhVnServiceImpl(NhanhVnRepository nhanhVnRepository) {
        super(nhanhVnRepository);
        this.nhanhVnRepository = nhanhVnRepository;
    }

    @Override
    public NhanhVnModel findByBiggestPriority() {
        return nhanhVnRepository.findByBiggestPriority();
    }

    @Override
    public NhanhVnModel create(NhanhVnDTO nhanhVnDTO) {
        NhanhVnModel nhanhVnModel = nhanhVnConverter.toModel(nhanhVnDTO);
        if (nhanhVnDTO.getPriority() == null) {
            nhanhVnModel.setPriority(0);
        }
        nhanhVnModel.setStatus(Status.ACTIVE);
        nhanhVnRepository.save(nhanhVnModel);
        return nhanhVnModel;
    }

    @Override
    public void update(NhanhVnModel nhanhVnModel, NhanhVnDTO nhanhVnDTO) {
        nhanhVnConverter.update(nhanhVnModel, nhanhVnDTO);
        nhanhVnRepository.save(nhanhVnModel);
    }

    @Override
    public NhanhVnModel getAccessToken() {
        NhanhVnModel nhanhVnModel = nhanhVnRepository.findByBiggestPriority();
        return nhanhVnModel;
    }

}
