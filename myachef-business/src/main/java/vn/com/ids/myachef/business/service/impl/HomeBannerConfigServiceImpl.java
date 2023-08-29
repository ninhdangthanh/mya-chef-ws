package vn.com.ids.myachef.business.service.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.service.HomeBannerConfigService;
import vn.com.ids.myachef.dao.model.HomeBannerConfigModel;
import vn.com.ids.myachef.dao.repository.HomeBannerConfigRepository;

@Service
@Transactional
public class HomeBannerConfigServiceImpl extends AbstractService<HomeBannerConfigModel, Long> implements HomeBannerConfigService {

    private HomeBannerConfigRepository homeBannerConfigRepository;

    protected HomeBannerConfigServiceImpl(HomeBannerConfigRepository homeBannerConfigRepository) {
        super(homeBannerConfigRepository);
        this.homeBannerConfigRepository = homeBannerConfigRepository;
    }

    @Override
    public HomeBannerConfigModel findByIsInterstitialAdTrue() {
        return homeBannerConfigRepository.findByIsInterstitialAdTrue();
    }
}
