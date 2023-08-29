package vn.com.ids.myachef.business.cwp.client;

import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import vn.com.ids.myachef.business.cwp.client.config.CWPConfiguration;
import vn.com.ids.myachef.business.cwp.client.type.CWPActionTypeEnum;
import vn.com.ids.myachef.business.cwp.client.type.CWPDomainTypeEnum;

/**
 * Class service to deal with ssl (Domain and Sub Domain)
 * 
 */
public class CWPSSLService {

	/**
	 * Add an request to Letsencrypt to add ssl to domain or subdomain
	 * 
	 * @param name : Domain name of Sub domain name
	 * @return Json
	 */
	public String addSSL(String name) {

		MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.ADD, CWPDomainTypeEnum.DOMAIN);
		body.field("name", name);

		return body.asString().getBody();
	}

	/**
	 * Delete ssl licence to domain or subdomain
	 * 
	 * @param name : Domain name of Sub domain name
	 * @return Json
	 */
	public String deleteSSL(String name) {
		MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.DELETE, CWPDomainTypeEnum.DOMAIN);
		body.field("name", name);

		return body.asString().getBody();
	}

	/**
	 * Return all SSL from a user
	 * 
	 * @return Json
	 */
	public String listSSL() {
		MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.LIST, CWPDomainTypeEnum.DOMAIN);
		return body.asString().getBody();
	}

	protected MultipartBody buildDefaultRequest(CWPActionTypeEnum actionTypeEnum, CWPDomainTypeEnum domainTypeEnum) {
		return Unirest.post(CWPConfiguration.getInstance().getSslURL())
				.header("Content-Type", "application/x-www-form-urlencoded")
				.field("key", CWPConfiguration.getInstance().getKey())
				.field("action", actionTypeEnum.getType())
				.field("user", CWPConfiguration.getInstance().getTargetUser());
	}
}
