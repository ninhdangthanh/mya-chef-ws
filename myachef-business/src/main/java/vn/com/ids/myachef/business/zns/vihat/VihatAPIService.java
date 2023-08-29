package vn.com.ids.myachef.business.zns.vihat;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.VihatConfiguration;
import vn.com.ids.myachef.business.zns.vihat.response.VihatOtpResponse;

@Service
@Transactional
@Slf4j
public class VihatAPIService {

    @Autowired
    private VihatConfiguration vihatConfiguration;

    public VihatOtpResponse message(String mesageObject) {
        log.info(mesageObject);
        log.info(vihatConfiguration.getVihatZaloApi());

        return Unirest.post(vihatConfiguration.getVihatZaloApi()) //
                .header("Content-Type", "application/json") //
                .body(mesageObject) //
                .asObject(VihatOtpResponse.class) //
                .getBody();
    }

}
