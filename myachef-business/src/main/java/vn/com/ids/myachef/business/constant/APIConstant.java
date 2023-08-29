package vn.com.ids.myachef.business.constant;

public class APIConstant {

    private APIConstant() {
        throw new IllegalStateException("Utility class");
    }

    // exception message
    public static final String NOT_FOUND_EXCEPTION_MESSAGE = "Not found %s with id: %s";
    public static final String NOT_FOUND_EXCEPTION_MESSAGE_WITH_ANOTHER_FIELD = "Not found %s with %s: %s";

    // object name
    public static final String BUSINESS_LINE = "BusinessLine";
    public static final String COMPANY = "Company";
    public static final String CUSTOMER = "Customer";
    public static final String CUSTOMER_GROUP = "Customer Group";
    public static final String CUSTOMER_UPLOAD = "Customer Upload";
    public static final String EMPLOYEE = "Employee";
    public static final String EVENT_HISTORY = "Event History";
    public static final String NOTE = "Note";
    public static final String SOCIAL_ACCOUNT = "Social Account";
    public static final String SOCIAL_ACCOUNT_INFO = "Social Account Information";
    public static final String TASK = "Task";
    public static final String TASK_HISTORY = "Task History";
    public static final String TASK_UPLOAD = "Task Upload";
    public static final String POST = "Post";
    public static final String EMAILCONFIG = "EmailConfig";
    public static final String NOTIFYCATION = "Notifycation";
    public static final String MEDIA_STORAGE = "MediaStorage";
    public static final String TREATMENT = "Treatment";
    public static final String TREATMENT_COURSE = "Treatment Course";
    public static final String TREATMENT_UPLOAD = "Treatment Upload";
    public static final String ZNSTEMPLATE = "ZNSTemplate";
    public static final String WORKSPACE = "Workspace";
    public static final String BROADCAST = "Broadcast";
    public static final String WORKSPACE_METADATA = "Workspace metadata";
    public static final String SUBSCRIPTION = "Subscription";
    public static final String SUBSCRIPTION_PLAN = "Subscription plan";
    public static final String ROLE = "Role";
    public static final String GROUP_ROLE = "Group Role";

    public static final int DEFAUT_PASSWORD_LENGTH = 6;

    public static final String FREE_VERSION = "Free";
    public static final Integer MOMO_PAYMENT_SUCCESS_STATUS = 0;

    // date pattern
    public static final String ASIA_HO_CHI_MINH_TIME_ZONE = "Asia/Ho_Chi_Minh";
    public static final String YYYY_MM_DD_T_HH_MM_SS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String YYYY_MM_DD_PATTERN = "yyyy-MM-dd";
    public static final String HH_MM_PATTERN = "HH:mm";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

    public static final String MARKETING_CAMPAIGN = "Marketing campaign";
    public static final String MARKETING_CAMPAIGN_DETAIL = "Marketing campaign detail";
    public static final Object MARKETING_CAMPAIGN_CUSTOMER = "Marketing campaign customer";

    public static final String ZALO_ACCOUNT = "Zalo account";

    public static final String DESKTOP_CONFIG = "Desktop Config";
    public static final String USER_BILLING = "User Billing";

    public static final String ZALO_NOTIFICATION = "Zalo notification";
}
