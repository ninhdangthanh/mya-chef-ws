package vn.com.ids.myachef.business.constant;

public class GraphAttributeConstant {

    private GraphAttributeConstant() {
        throw new IllegalStateException("GraphAttributeConstant class");
    }

    // Customer graph
    public static final String CUSTOMER_BUSINESS_LINE_GRAPH = "businessLine";
    public static final String CUSTOMER_EVENT = "events";
    public static final String CUSTOMER_NOTE = "notes";
    public static final String CUSTOMER_TASK = "tasks";
    public static final String CUSTOMER_CUSTOMER_GROUP_GRAPH = "customerGroups";
    public static final String CUSTOMER_GRAPH = "customers";

    // Employee graph
    public static final String EMPLOYEE_COMPANY_GRAPH = "company";
    public static final String EMPLOYEE_EMPLOYEE_MANAGE_SOCIAL_GRAPH = "employeeManageSocials";
    public static final String EMPLOYEE_EVENT_GRAPH = "events";
    public static final String EMPLOYEE_NOTE_GRAPH = "notes";
    public static final String EMPLOYEE_POST_CREATED_GRAPH = "postsCreateds";
    public static final String EMPLOYEE_POST_UPDATED_GRAPH = "postsUpdateds";
    public static final String EMPLOYEE_ASSIGNED_TASK_GRAPH = "assignedTasks";
    public static final String EMPLOYEE_CREATED_TASK_GRAPH = "createdTasks";
    public static final String EMPLOYEE_UPDATED_TASK_GRAPH = "updatedTasks";
    public static final String ROLE_GRAPH = "roleModel";

    // Task graph
    public static final String TASK_CUSTOMER_GRAPH = "customer";
    public static final String TASK_CREATED_BY_GRAPH = "createdBy";
    public static final String TASK_UPDATED_BY_GRAPH = "updatedBy";
    public static final String TASK_ASSIGNEE_GRAPH = "assignee";
    public static final String TASK_TASK_HISTORIES_GRAPH = "taskHistories";

    // Post graph
    public static final String POST_DISTRIBUTION_GRAPH = "distributions";
    public static final String POST_ASSIGNEE_GRAPH = "assignee";
    public static final String POST_CREATEDBY_GRAPH = "createdBy";
    public static final String POST_UPDATEDBY_GRAPH = "updatedBy";
    public static final String POST_APPROVEDBY_GRAPH = "approvedBy";

    // Social Account graph
    public static final String SOCIAL_ACCOUNT_DISTRIBUTION_GRAPH = "distributions";
    public static final String SOCIAL_ACCOUNT_EMPLOYEE_MANAGE_SOCIAL_GRAPH = "employeeManageSocials";

    // Post publishing graph
    public static final String POST_PUBLISHING_DISTRIBUTION_GRAPH = "distributions";

    // Distribution graph
    public static final String DISTRIBUTION_POST_GRAPH = "post";
    public static final String DISTRIBUTION_SOCIAL_ACCOUNT_GRAPH = "socialAccount";

    // MediaStorage graph
    public static final String MEDIA_STORAGE_FILE_SOCIAL = "fileUploadSocials";
    public static final String MEDIA_STORAGE_POST = "posts";

    // fileUpload graph
    public static final String FILE_UPLOAD_GRAPH = "fileUploads";

    // marketing campaign
    public static final String MARKETING_CAMPAIGN_DETAIL = "marketingCampaignDetails";
    public static final String MARKETING_CAMPAIGN_CUSTOMER = "marketingCampaignCustomers";

    // subscription Plan
    public static final String SUBSCRIPTION_PLAN = "subscriptionPlan";
}
