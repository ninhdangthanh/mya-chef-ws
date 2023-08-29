package vn.com.ids.myachef.business.cwp.client.type;

public enum CWPActionTypeEnum {
	ADD("add"), DELETE("del"), LIST("list");

	private String type;

	private CWPActionTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
