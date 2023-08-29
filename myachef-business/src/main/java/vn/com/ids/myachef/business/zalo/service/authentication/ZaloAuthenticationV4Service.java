package vn.com.ids.myachef.business.zalo.service.authentication;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ZaloConfiguration;

@Service
@Transactional
@Slf4j
public class ZaloAuthenticationV4Service {
    private static final String CODE_VERIFIER = "code_verifier";
    private static final String GRANT_TYPE = "grant_type";
    private static final String APP_ID = "app_id";
    private static final String CODE = "code";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String SECRET_KEY = "secret_key";
    private static final String REFRESH_TOKEN = "refresh_token";

    @Autowired
    private ZaloConfiguration zaloConfiguration;

    // @Autowired
    // private SocialAccountService socialAccountService;
    //
    // public ZaloToken getAccessToken(SocialAccountModel socialAccountModel) {
    // return Unirest.post(zaloConfiguration.getZaloOAOauthUrl()) //
    // .header(SECRET_KEY, socialAccountModel.getSecretKey()) //
    // .header(CONTENT_TYPE, "application/x-www-form-urlencoded") //
    // .field(CODE, socialAccountModel.getAuthorizationCode()) //
    // .field(APP_ID, socialAccountModel.getAppId()) //
    // .field(GRANT_TYPE, "authorization_code") //
    // .field(CODE_VERIFIER, zaloConfiguration.getZaloCodeVerifier()) //
    // .asObject(ZaloToken.class) //
    // .getBody();
    // }
    //
    // private ZaloToken getAccessTokenFromRefreshToken(SocialAccountModel socialAccountModel) {
    // return Unirest.post(zaloConfiguration.getZaloOAOauthUrl()) //
    // .header(SECRET_KEY, socialAccountModel.getSecretKey()) //
    // .header(CONTENT_TYPE, "application/x-www-form-urlencoded") //
    // .field(REFRESH_TOKEN, socialAccountModel.getRefreshToken()) //
    // .field(APP_ID, socialAccountModel.getAppId()) //
    // .field(GRANT_TYPE, REFRESH_TOKEN) //
    // .asObject(ZaloToken.class) //
    // .getBody();
    // }
    //
    // public void refreshAccessToken(SocialAccountModel socialAccount) {
    // if (socialAccount.getExpirationTime().isBefore(LocalDateTime.now())) {
    // ZaloToken zaloToken = getAccessTokenFromRefreshToken(socialAccount);
    //
    // if (zaloToken.getAccessToken() != null) {
    // socialAccount.setAccessToken(zaloToken.getAccessToken());
    // socialAccount.setRefreshToken(zaloToken.getRefreshToken());
    // socialAccount.setExpirationTime(LocalDateTime.now().plusSeconds(zaloConfiguration.getZaloAccesstokenExpiryTime()));
    //
    // log.info("Get new accessToken from refreshToken: " + zaloToken.getAccessToken());
    // } else {
    // socialAccount.setStatus(SocialAccountStatus.EXPIRED);
    // log.info("RefreshToken expired: " + zaloToken.getErrorCode());
    // }
    // socialAccountService.save(socialAccount);
    // }
    // }

}
