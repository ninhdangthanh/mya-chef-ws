package vn.com.ids.myachef.business.utils;

public class RoleUtils {

    private RoleUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String CAMPAIGN_KEY = "PERSONAL_ZALO_ACCOUNT/MARKETING_CAMPAIGN";

    // public static Map<String, Map<PermissionType, Boolean>> convertPermissionsToPermissionMap(List<PermissionModel>
    // permissionModels, String pattern,
    // boolean isSubscriptionExpired) {
    // List<PermissionModel> permissions = new ArrayList<>();
    // for (PermissionModel child : permissionModels) {
    // child.setKey(child.getName());
    // permissions.add(child);
    // getChildren(child, permissions, child.getName(), pattern);
    // }
    //
    // LinkedHashMap<String, Map<PermissionType, Boolean>> roleMap = new LinkedHashMap<>();
    // for (PermissionModel child : permissions) {
    // if (!CollectionUtils.isEmpty(child.getPermissions())) {
    // Map<PermissionType, Boolean> currentPermissionsMap = child.getPermissions();
    // Map<PermissionType, Boolean> permissionsMap = new HashMap<>();
    // for (Map.Entry<PermissionType, Boolean> entry : currentPermissionsMap.entrySet()) {
    // permissionsMap.put(entry.getKey(), entry.getValue());
    // }
    // if (isSubscriptionExpired && child.getKey().equals(CAMPAIGN_KEY)) {
    // permissionsMap.computeIfPresent(PermissionType.CREATE, (key, value) -> false);
    // permissionsMap.computeIfPresent(PermissionType.UPDATE, (key, value) -> false);
    // permissionsMap.computeIfPresent(PermissionType.DELETE, (key, value) -> false);
    // }
    // roleMap.put(child.getKey(), permissionsMap);
    // }
    // }
    //
    // return roleMap;
    // }
    //
    // private static void getChildren(PermissionModel permission, List<PermissionModel> permissions, String name, String
    // pattern) {
    // if (CollectionUtils.isEmpty(permission.getChild())) {
    // return;
    // }
    // for (PermissionModel child : permission.getChild()) {
    // String path = String.format(pattern, name, child.getName());
    // child.setKey(path);
    // permissions.add(child);
    //
    // if (!CollectionUtils.isEmpty(child.getChild())) {
    // for (PermissionModel grandchildren : child.getChild()) {
    // String childPath = String.format(pattern, child.getKey(), grandchildren.getName());
    // grandchildren.setKey(childPath);
    // permissions.add(grandchildren);
    // }
    // }
    // }
    // }
}
