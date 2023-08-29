package vn.com.ids.myachef.business.cwp.client.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kong.unirest.Unirest;

public class CWPConfiguration {

    private String domainURL;
    private String sslURL;
    private String key;
    private String targetUser;
    private String targetPath;
    private boolean autossl;
    private boolean unirestVerifySsl;

    private static final CWPConfiguration instance = new CWPConfiguration();

    private static final Logger logger = LoggerFactory.getLogger(CWPConfiguration.class);

    static {
        try (InputStream input = CWPConfiguration.class.getClassLoader().getResourceAsStream("cwp-client.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                logger.error("Sorry, unable to find cwp-client.properties");

                throw new RuntimeException("Sorry, unable to find cwp-client.properties");
            }

            prop.load(input);

            instance.domainURL = prop.getProperty("cwp.url.domain");
            instance.sslURL = prop.getProperty("cwp.url.ssl");
            instance.key = prop.getProperty("cwp.key");
            instance.targetUser = prop.getProperty("cwp.target.user");
            instance.targetPath = prop.getProperty("cwp.target.path");
            instance.autossl = "1".equalsIgnoreCase(prop.getProperty("cwp.autossl"));
            instance.unirestVerifySsl = "true".equalsIgnoreCase(prop.getProperty("unirest.verify-ssl"));

            logger.info(instance.toString());

            Unirest.config().verifySsl(false);

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private CWPConfiguration() {

    }

    public static CWPConfiguration getInstance() {
        return instance;
    }

    public String getDomainURL() {
        return domainURL;
    }

    public String getSslURL() {
        return sslURL;
    }

    public String getKey() {
        return key;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public boolean isAutossl() {
        return autossl;
    }

    public boolean isUnirestVerifySsl() {
        return unirestVerifySsl;
    }

    @Override
    public String toString() {
        return "CWPConfiguration [domainURL=" + domainURL + ", sslURL=" + sslURL + ", key=" + key + ", targetUser=" + targetUser + ", targetPath=" + targetPath
                + ", autossl=" + autossl + ", unirestVerifySsl=" + unirestVerifySsl + "]";
    }
}
