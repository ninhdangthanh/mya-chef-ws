package vn.com.ids.myachef.business.service.nhanhvn.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.service.nhanhvn.NhanhVNShippingService;
import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVnLocationRequest;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNLocationResponse;
import vn.com.ids.myachef.dao.model.NhanhVnModel;

@Service
@Transactional
@Slf4j
public class NhanhVNShippingServiceImpl extends NhanhVNAbstractServiceImpl implements NhanhVNShippingService {

    @Override
    public NhanhVNLocationResponse getLocation(NhanhVnLocationRequest nhanhVnLocationRequest) {

        String localtionObject = toJson(nhanhVnLocationRequest);

        NhanhVNLocationResponse nhanhVNLocationResponse = null;
        NhanhVnModel nhanhVnModel = nhanhVnService.findByBiggestPriority();
        if (nhanhVnModel != null) {
            nhanhVNLocationResponse = Unirest.post("https://open.nhanh.vn/api/shipping/location")//
                    .multiPartContent()//
                    .field(VERSION, nhanhVnModel.getVersion())//
                    .field(APP_ID, nhanhVnModel.getAppId())//
                    .field(BUSINESS_ID, nhanhVnModel.getBusinessId())//
                    .field(ACCESS_TOKEN, nhanhVnModel.getAccessToken())//
                    .field(DATA, localtionObject)//
                    .asObject(NhanhVNLocationResponse.class)//
                    .getBody();
        } else {
            log.error("Can not get location");
        }
        return nhanhVNLocationResponse;

    }

}
