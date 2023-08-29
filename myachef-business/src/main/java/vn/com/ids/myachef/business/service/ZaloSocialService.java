package vn.com.ids.myachef.business.service;

import vn.com.ids.myachef.business.zalo.social.UserProfile;
import vn.com.ids.myachef.business.zalo.social.ZaloUser;

public interface ZaloSocialService {

    public ZaloUser getUserInfoByAccessToken(String accessToken);

    public UserProfile getProfile(String accessToken, String code);
}
