package vn.com.ids.myachef.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.payload.reponse.momo.MomoPaymentRedirectBody;
import vn.com.ids.myachef.business.service.MomoPaymentService;

@RestController
@RequestMapping("/api/momo-payment")
@Slf4j
public class MomoPaymentController {

    @Autowired
    private MomoPaymentService momoPaymentService;

    @Operation(summary = "Momo payment redirect url")
    @PostMapping("/redirect-url")
    public void handleRedirect(@RequestBody MomoPaymentRedirectBody body) {
        log.info("------------------ Momo payment redirect url - START ----------------");
        if (body != null) {
            momoPaymentService.handleRedirect(body);
        }
    }
}
