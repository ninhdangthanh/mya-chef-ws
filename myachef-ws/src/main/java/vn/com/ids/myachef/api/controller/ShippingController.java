package vn.com.ids.myachef.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.service.nhanhvn.NhanhVNShippingService;
import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVnLocationRequest;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNLocationResponse;

@RestController
@RequestMapping("/api/shipping")
@Slf4j
public class ShippingController {

    @Autowired
    private NhanhVNShippingService nhanhVNShippingService;

    @GetMapping("/location")
    public NhanhVNLocationResponse getLocation(@Valid NhanhVnLocationRequest nhanhVnLocationRequest) {
        log.info("--------------- ShippingController - GET LOCATION - START --------------------");
        return nhanhVNShippingService.getLocation(nhanhVnLocationRequest);
    }

}
