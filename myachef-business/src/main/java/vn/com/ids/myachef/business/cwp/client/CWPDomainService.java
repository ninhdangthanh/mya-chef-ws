package vn.com.ids.myachef.business.cwp.client;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import vn.com.ids.myachef.business.cwp.client.config.CWPConfiguration;
import vn.com.ids.myachef.business.cwp.client.type.CWPActionTypeEnum;
import vn.com.ids.myachef.business.cwp.client.type.CWPDomainTypeEnum;

/**
 * Class service to deal with Domain and Sub Domain
 */
@Service
@Transactional
public class CWPDomainService {

    private static final Logger logger = LoggerFactory.getLogger(CWPDomainService.class);

    /**
     * Add a domain
     * 
     * @param domainName.
     *            It should be e.g: 'abc.com'
     * @return Json
     */
    public String addDomain(String domainName) {
        logger.info("Add Domain Start: {}", domainName);
        MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.ADD, CWPDomainTypeEnum.DOMAIN);
        body.field("name", domainName).field("path", CWPConfiguration.getInstance().getTargetUser());

        logger.info("Connect to CWP");
        return body.asString().getBody();
    }

    /**
     * Delete a domain
     * 
     * @param domainName
     * @return Json
     */
    public String deleteDomain(String domainName) {
        logger.info("Delete Domain Start: {}", domainName);

        MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.DELETE, CWPDomainTypeEnum.DOMAIN);
        body.field("name", domainName);

        logger.info("Connect to CWP");

        return body.asString().getBody();
    }

    /**
     * List all domains from configured user
     * 
     * @return Json
     */
    public String listDomain() {
        logger.info("List Domain Start");
        MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.LIST, CWPDomainTypeEnum.DOMAIN);
        logger.info("Connect to CWP");
        return body.asString().getBody();
    }

    /**
     * Add a sub domain
     * 
     * @param subdomain
     *            name. It should be e.g: 'abc.addycrm.com'
     * @return Json
     */
    public String addSubDomain(String subdomain) {
        logger.info("Add SUB Domain Start : {}", subdomain);
        MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.ADD, CWPDomainTypeEnum.SUBDOMAIN);
        body.field("name", subdomain).field("path", CWPConfiguration.getInstance().getTargetPath());
        logger.info("Connect to CWP");
        return body.asString().getBody();
    }

    /**
     * Delete a domain by name
     * 
     * @param subdomain
     * @return Json
     */
    public String deleteSubDomain(String subdomain) {

        logger.info("Delete SUB Domain Start, {}", subdomain);
        MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.DELETE, CWPDomainTypeEnum.SUBDOMAIN);
        body.field("name", subdomain);
        logger.info("Connect to CWP");
        return body.asString().getBody();
    }

    /**
     * List all sub domain of a user
     * 
     * @return Json
     */
    public String listSubDomain() {
        logger.info("List SUB Domain Start");
        MultipartBody body = buildDefaultRequest(CWPActionTypeEnum.LIST, CWPDomainTypeEnum.SUBDOMAIN);
        logger.info("Connect to CWP");
        return body.asString().getBody();
    }

    protected MultipartBody buildDefaultRequest(CWPActionTypeEnum actionTypeEnum, CWPDomainTypeEnum domainTypeEnum) {
        return Unirest.post(CWPConfiguration.getInstance().getDomainURL()) //
                .header("Content-Type", "application/x-www-form-urlencoded") //
                .field("key", CWPConfiguration.getInstance().getKey()) //
                .field("action", actionTypeEnum.getType()) //
                .field("user", CWPConfiguration.getInstance().getTargetUser()) //
                .field("type", domainTypeEnum.getType()) //
                // .field("path", "/applications/addy-web-app") //
                .field("autossl", CWPConfiguration.getInstance().isAutossl() ? "1" : "0");
    }
}
