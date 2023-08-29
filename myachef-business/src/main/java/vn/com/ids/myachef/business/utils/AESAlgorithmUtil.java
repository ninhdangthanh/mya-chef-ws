package vn.com.ids.myachef.business.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.message.UtilsErrorMessage;

@Slf4j
public class AESAlgorithmUtil {

    private static final String ALGO = "AES";
    private static final String PROP_FILE_NAME = "aes.properties";
    private static final String SECRET_KEY = getAESAlgorithmFromProperties();

    private AESAlgorithmUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static String getAESAlgorithmFromProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = AESAlgorithmUtil.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME)) {
            properties.load(inputStream);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return properties.getProperty("aesalAlgorithm.SecretKey");
    }

    public static String encrypt(String data) {
        String encodeStr = "";
        try {
            Key key = generateKey(encodeKey(SECRET_KEY));
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = cipher.doFinal(data.getBytes());
            encodeStr = Base64.getEncoder().encodeToString(encVal);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return encodeStr;
    }

    public static String decrypt(String encryptStr) {
        try {
            Key key = generateKey(encodeKey(SECRET_KEY));
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptStr)));
        } catch (Exception e) {
            throw new BadRequestException(UtilsErrorMessage.INVALID_DECRYPT_TEXT.message);
        }
    }

    private static Key generateKey(String secret) {
        byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
        return new SecretKeySpec(decoded, ALGO);
    }

    public static String decodeKey(String str) {
        byte[] decoded = Base64.getDecoder().decode(str.getBytes());
        return new String(decoded);
    }

    private static String encodeKey(String str) {
        byte[] encoded = Base64.getEncoder().encode(str.getBytes());
        return new String(encoded);
    }

}
