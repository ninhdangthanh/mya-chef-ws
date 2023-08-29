package vn.com.ids.myachef.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.service.ZaloSocialService;
import vn.com.ids.myachef.business.zalo.social.UserProfile;
import vn.com.ids.myachef.business.zalo.social.ZaloUser;

@Service
@Transactional
public class ZaloSocialServiceImpl implements ZaloSocialService {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String CODE = "code";
    private static final String SECRET_KEY = "secret_key";

    @Autowired
    private ApplicationConfig applicationConfig;

    @Override
    public ZaloUser getUserInfoByAccessToken(String accessToken) {
        HttpResponse<ZaloUser> response = Unirest.get("https://graph.zalo.me/v2.0/me?fields=id,name,picture")//
                .header(ACCESS_TOKEN, accessToken)//
                .asObject(ZaloUser.class);
        return response.getBody();
    }

    @Override
    public UserProfile getProfile(String accessToken, String code) {
        HttpResponse<UserProfile> response = Unirest.get("https://graph.zalo.me/v2.0/me/info")//
                .header(ACCESS_TOKEN, accessToken)//
                .header(CODE, code)//
                .header(SECRET_KEY, applicationConfig.getZaloSecretKey())//
                .asObject(UserProfile.class);
        return response.getBody();
    }
}
