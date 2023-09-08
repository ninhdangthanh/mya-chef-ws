package vn.com.ids.myachef.business.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Getter
@Configuration
@ToString
public class ApplicationConfig {

    private static final String CUSTOMER_AVATAR_PATH = "customers/";
    private static final String CUSTOMER_ATTACHMENT_PATH = "attachments/";
    private static final String POST_ATTACHMENT_PATH = "post-attachments/";
    private static final String POST_PHOTO_COVER_PATH = "posts/";
    private static final String MEDIA_STORAGE_PATH = "medias/";
    private static final String TREATMENT_STORAGE_PATH = "treatments/";
    private static final String TASK_STORAGE_PATH = "tasks/";
    private static final String SOCIAL_ACCOUNT_STORAGE_PATH = "social-account/";
    private static final String TEMPLATES_STORAGE_PATH = "templates/";
    private static final String TEMPLATE_IMAGE_PATH = "templates/images";
    private static final String FILE_UPLOAD = "file-uploads/";
    private static final String ZALO_NOTIFICATION = "zalo_notification/";

    private static final String EXCEL_NOTICE = "excel-notice/";
    private static final String EXCEL_SAMPLE_DATA = "excel-sample-data/";

    private static final String BANNER_PATH = "banner/";
    private static final String ORDER_PATH = "order/";
    private static final String CATEGORY_PATH = "category/";
    private static final String SUBSCRIPTION_PATH = "subscription/";
    private static final String BILL_PATH = "bill/";
    private static final String INGREDIENT_CATEGORY = "ingredient-category/";
    private static final String INGREDIENT = "ingredient/";

    // upload file
    @Value("${app.storage.path}")
    private String fileUploadPath;

    // jwt token
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expirationMs}")
    private Long jwtExpirationMs;

    @Value("${app.jwt.register.expirationMs}")
    private Long jwtRegisterExpirationMs;

    @Value("${app.jwt.refresh.token.expirationMs}")
    private Long jwtRefreshTokenExpirationMs;

    // default email
    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private int smtpPort;

    @Value("${mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private boolean smtpEnableTls;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    // react redirect url
    @Value("${react.redirect.url}")
    private String reactRedirectUrl;

    @Value("${edit.post.browser.url}")
    private String editPostURL;

    // CORS allowed origins
    @Value("${cors.allowed.domains}")
    private String corsAllowedDomains;

    // Spring batch
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Value("${app.default.workspace}")
    private String defaultWorkspace;

    @Value("${otp.expiry.time}")
    private long otpExpiryTime;

    @Value("${token.expiry.time}")
    private long tokenExpiryTime;

    @Value("${workspace.invite.code.expiry.time}")
    private long workspaceInviteCodeExpiryTime;

    @Value("${addy-bds.profile.url}")
    private String addyBdsUrl;

    @Value("${app.file.zip.path}")
    private String zipAppFilePath;

    @Value("${domain.suffix}")
    private String domainSuffix;

    @Value("${enable.add.subdomain}")
    private boolean enableAddSubdomain;

    @Value("${app.trial.days:3}")
    private int appTrialDays;

    @Value("#{'${admin.recipient.email}'.split(',')}")
    private List<String> sendTos;

    @Value("${marketing.campaign.statistic.limit.day}")
    private Integer statisticLimitDay;

    @Value("${marketing.campaign.statistic.delay.day}")
    private Integer statisticDelayDay;

    @Value("${client.period.time}")
    private String clientPeriodTime;

    @Value("${admin.period.time}")
    private String adminPeriodTime;

    @Value("${total.hour.change.subscription}")
    private int totalHourChangeSubscription;

    @Value("${notification.warning.percent}")
    private int notificationWarningPercent;

    @Value("${notification.warning.days}")
    private int notificationWarningDays;

    @Value("${subscription.extend.late.day}")
    private int subscriptionExtendLateDay;

    @Value("${nhanh.access.token.url}")
    private String nhanhAccessTokenUrl;

    @Value("${nhanh.vn.base.url}")
    private String nhanhVnBaseUrl;

    @Value("${nhanh.vn.product.category.url}")
    private String nhanhVnProductCategoryUrl;

    @Value("${nhanh.vn.product.url}")
    private String nhanhVnProductUrl;

    @Value("${nhanh.vn.product.detail.url}")
    private String nhanhVnProductDetailUrl;

    @Value("${zalo.secret.key}")
    private String zaloSecretKey;

    @Value("${default.minimun.price.for.free.ship}")
    private Long defaultMinimunPriceForFreeShip;

    @Value("${default.free.ship.price}")
    private Long defaultFreeShipPrice;

    @Value("${coin.ratio.convert}")
    private Long coinRatioConvert;

    public String getOrderPath() {
        return ORDER_PATH + "%s/";
    }

    public String getFullOrderPath() {
        return fileUploadPath + ORDER_PATH + "%s/";
    }

    public String getFullCustomerAvatarPath() {
        return fileUploadPath + CUSTOMER_AVATAR_PATH + "%s/";
    }

    public String getFullCustomerAttachmentPath() {
        return fileUploadPath + CUSTOMER_AVATAR_PATH + "%s/" + CUSTOMER_ATTACHMENT_PATH;
    }

    public String getFullPostAttachmentPath() {
        return fileUploadPath + POST_ATTACHMENT_PATH + "%s/";
    }

    public String getFullPostPhotoCoverPath() {
        return fileUploadPath + POST_PHOTO_COVER_PATH + "%s/";
    }

    public String getFullMediaStoragePath() {
        return fileUploadPath + MEDIA_STORAGE_PATH + "%s/";
    }

    public String getFullTemplatesStoragePath() {
        return fileUploadPath + TEMPLATES_STORAGE_PATH + "%s/";
    }

    public String getFullTaskStoragePath() {
        return fileUploadPath + TASK_STORAGE_PATH + "%s/";
    }

    public String getFullSocialAccountStoragePath() {
        return fileUploadPath + SOCIAL_ACCOUNT_STORAGE_PATH + "%s/";
    }

    public String getTreatmentPath() {
        return fileUploadPath + CUSTOMER_AVATAR_PATH + "%s/" + TREATMENT_STORAGE_PATH;
    }

    public String getFullFileUploadPath() {
        return fileUploadPath + FILE_UPLOAD + "%s/";
    }

    public String getCustomerAvatarPath() {
        return CUSTOMER_AVATAR_PATH + "%s/";
    }

    public String getCustomerAttachmentPath() {
        return CUSTOMER_ATTACHMENT_PATH;
    }

    public String getPostAttachmentPath() {
        return POST_ATTACHMENT_PATH;
    }

    public String getPostPhotoCoverPath() {
        return POST_PHOTO_COVER_PATH;
    }

    public String getMediaStoragePath() {
        return MEDIA_STORAGE_PATH;
    }

    public String getTreatmentStoragePath() {
        return TREATMENT_STORAGE_PATH;
    }

    public String getTaskStoragePath() {
        return TASK_STORAGE_PATH;
    }

    public String getSocialAccountStoragePath() {
        return SOCIAL_ACCOUNT_STORAGE_PATH;
    }

    public String getTemplatesStoragePath() {
        return TEMPLATES_STORAGE_PATH;
    }

    public String getTemplateImagePath() {
        return TEMPLATE_IMAGE_PATH;
    }

    public String getFileUpload() {
        return FILE_UPLOAD;
    }

    public String getZaloNotification() {
        return ZALO_NOTIFICATION;
    }

    public String getFullZaloNotificationStoragePath() {
        return fileUploadPath + ZALO_NOTIFICATION + "%s/";
    }

    public String getExcelNotice() {
        return EXCEL_NOTICE;
    }

    public String getFullExcelNoticeStoragePath() {
        return fileUploadPath + EXCEL_NOTICE;
    }

    public String getExcelSampleData() {
        return EXCEL_SAMPLE_DATA;
    }

    public String getFullExcelSampleDataStoragePath() {
        return fileUploadPath + EXCEL_SAMPLE_DATA;
    }

    public int getAppTrialDays() {
        return appTrialDays;
    }

    public String getNhanhGetAllProductCategoryUrl() {
        return nhanhVnBaseUrl + nhanhVnProductCategoryUrl;
    }

    public String getNhanhGetAllProductUrl() {
        return nhanhVnBaseUrl + nhanhVnProductUrl;
    }

    public String getNhanhDetailProduct() {
        return nhanhVnBaseUrl + nhanhVnProductDetailUrl;
    }

    public String getFullBannerPath() {
        return fileUploadPath + BANNER_PATH;
    }

    public String getBannerPath() {
        return BANNER_PATH;
    }
    
    public String getFullIngredientCategoryPath() {
        return fileUploadPath + INGREDIENT_CATEGORY;
    }

    public String getIngredientCategoryPath() {
        return INGREDIENT_CATEGORY;
    }
    
    public String getFullIngredientPath() {
        return fileUploadPath + INGREDIENT;
    }

    public String getIngredientPath() {
        return INGREDIENT;
    }

    public String getFullBannerProductCategoryPath() {
        return fileUploadPath + BANNER_PATH + CATEGORY_PATH;
    }

    public String getBannerProductCategoryPath() {
        return BANNER_PATH + CATEGORY_PATH;
    }

    public String getFullSubscriptionPath() {
        return fileUploadPath + SUBSCRIPTION_PATH;
    }

    public String getSubscriptionPath() {
        return SUBSCRIPTION_PATH;
    }

    public String getFullSubscriptionBillPath() {
        return fileUploadPath + SUBSCRIPTION_PATH + BILL_PATH + "%s/";
    }

    public String getSubscriptionBillPath() {
        return SUBSCRIPTION_PATH + BILL_PATH + "%s/";
    }
}
