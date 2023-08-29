package vn.com.ids.myachef.business.zns.zalo;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ZaloConfiguration;
import vn.com.ids.myachef.business.zalo.broadcast.BroadcastArticleResponse;

@Service
@Transactional
@Slf4j
public class ZaloZNSAPIService {

    @Autowired
    private ZaloConfiguration zaloConfiguration;

    public BroadcastArticleResponse broadcastArticle(String accessToken, String mesageObject) {
        try {
            return Unirest.post(zaloConfiguration.getZaloZnsMessageUrl()) //
                    .header("Content-Type", "application/json") //
                    .header("access_token", accessToken)//
                    .body(mesageObject) //
                    .asObject(BroadcastArticleResponse.class) //
                    .getBody();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

}
