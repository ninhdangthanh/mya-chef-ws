package vn.com.ids.myachef.business.service.nhanhvn.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.service.nhanhvn.NhanhVNCustomerService;
import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVNCustomerRequest;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNCustomerResponse;
import vn.com.ids.myachef.dao.model.NhanhVnModel;

@Service
@Transactional
@Slf4j
public class NhanhVNCustomerServiceImpl extends NhanhVNAbstractServiceImpl implements NhanhVNCustomerService {

    @Override
    public NhanhVNCustomerResponse createOrUpdate(NhanhVNCustomerRequest customerRequest) {

        List<NhanhVNCustomerRequest> customerRequests = new ArrayList<>();
        customerRequests.add(customerRequest);

        String mesageObject = toJson(customerRequests);

        NhanhVnModel nhanhVnModel = nhanhVnService.findByBiggestPriority();
        NhanhVNCustomerResponse nhanhVNCustomerResponse = null;
        if (nhanhVnModel != null) {
            nhanhVNCustomerResponse = Unirest.post("https://open.nhanh.vn/api/customer/add")//
                    .multiPartContent()//
                    .field(VERSION, nhanhVnModel.getVersion())//
                    .field(APP_ID, nhanhVnModel.getAppId())//
                    .field(BUSINESS_ID, nhanhVnModel.getBusinessId())//
                    .field(ACCESS_TOKEN, nhanhVnModel.getAccessToken())//
                    .field(DATA, mesageObject)//
                    .asObject(NhanhVNCustomerResponse.class)//
                    .getBody();
        } else {
            log.error("Can not create customer");
        }
        return nhanhVNCustomerResponse;
    }

}
