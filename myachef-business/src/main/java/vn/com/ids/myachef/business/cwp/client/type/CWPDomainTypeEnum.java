package vn.com.ids.myachef.business.cwp.client.type;

public enum CWPDomainTypeEnum {
	DOMAIN("domain"), SUBDOMAIN("subdomain");

	private String type;

	private CWPDomainTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
