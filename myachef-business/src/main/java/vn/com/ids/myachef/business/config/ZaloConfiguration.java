package vn.com.ids.myachef.business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Configuration
@PropertySource(value = "classpath:zalo.properties")
public class ZaloConfiguration {

    // Zalo Acticle
    @Value("${zalo.article.list.url}")
    private String zaloArticleListUrl;

    @Value("${zalo.article.detail.url}")
    private String zaloArticleDetailUrl;

    @Value("${zalo.article.create.url}")
    private String zaloArticleCreateUrl;

    @Value("${zalo.article.verify.url}")
    private String zaloArticleVerifyUrl;

    @Value("${zalo.article.update.url}")
    private String zaloArticleUpdateUrl;

    @Value("${zalo.article.delete.url}")
    private String zaloArticleDeleteUrl;

    @Value("${zalo.article.video.upload.url}")
    private String zaloArticleVideoUploadUrl;

    @Value("${zalo.article.video.verify.url}")
    private String zaloArticleVideoVerifyUrl;

    // Zalo OA
    @Value("${zalo.zns.message.url}")
    private String zaloZnsMessageUrl;

    @Value("${zalo.oa.oauth.url}")
    private String zaloOAOauthUrl;

    @Value("${zalo.accesstoken.expiry.time}")
    private long zaloAccesstokenExpiryTime;

    // Zalo Account
    @Value("${zalo.account.info.url}")
    private String zaloAccountInfoUrl;

    @Value("${zalo.avatar.domain}")
    private String zaloAvatarDomain;

    // zalo authentication v4
    @Value("${zalo.code.verifier}")
    private String zaloCodeVerifier;

    @Value("${zalo.code.challenge}")
    private String zaloCodeChallenge;

    // Zalo authentication v4 redirect url
    @Value("${zalo.react.redirect.url}")
    private String zaloReactRedirectUrl;

    // Zalo assistant
    @Value("${zalo.assistant.workspace.suffix}")
    private String zaloWorkspaceSuffix;

    @Value("${zalo.assistant.userid.suffix}")
    private String zaloUserIdSuffix;

    @Value("${zalo.assistant.task.redirect.url}")
    private String zaloAsstTaskRedirectUrl;

    @Value("${zalo.assistant.dashboard.redirect.url}")
    private String zaloAsstDashboardRedirectUrl;

    @Value("${zalo.assistant.contact.url}")
    private String zaloAssistantContactUrl;

    // Zalo Desktop
    @Value("${zalo.marketing.campaign.workspace.suffix}")
    private String zaloMarketingCampaignWorkspaceSuffix;
}
