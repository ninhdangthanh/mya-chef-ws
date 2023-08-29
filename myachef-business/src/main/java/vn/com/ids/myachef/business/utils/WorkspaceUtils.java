package vn.com.ids.myachef.business.utils;

public class WorkspaceUtils {
	public static String decode(String workspace) {
		return AESAlgorithmUtil.decrypt(workspace);
	}
}
