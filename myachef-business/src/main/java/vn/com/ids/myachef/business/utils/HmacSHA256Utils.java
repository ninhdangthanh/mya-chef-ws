package vn.com.ids.myachef.business.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HmacSHA256Utils {

    private HmacSHA256Utils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String PROP_FILE_NAME = "momo.properties";
    private static final String SECRET_KEY = getMomoSecrectKey();

    public static String encrypt(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256HMAC.init(secretKey);

        return Hex.encodeHexString(sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private static String getMomoSecrectKey() {
        Properties properties = new Properties();
        try (InputStream inputStream = AESAlgorithmUtil.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME)) {
            properties.load(inputStream);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return properties.getProperty("momo.secret.key");
    }

}
