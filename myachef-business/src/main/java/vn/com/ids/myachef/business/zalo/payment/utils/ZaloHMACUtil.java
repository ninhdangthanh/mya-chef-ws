/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ids.myachef.business.zalo.payment.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZaloHMACUtil {

    private ZaloHMACUtil() {
        throw new IllegalStateException("Utility class");
    }

    // @formatter:off
    public static final String HMACMD5 = "HmacMD5";
    public static final String HMACSHA1 = "HmacSHA1";
    public static final String HMACSHA256 = "HmacSHA256";
    public static final String HMACSHA512 = "HmacSHA512";
    public static final Charset UTF8CHARSET = StandardCharsets.UTF_8;

    public static byte[] hMacEncode(final String algorithm, final String key, final String data) {
        Mac macGenerator = null;
        try {
            macGenerator = Mac.getInstance(algorithm);
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(UTF8CHARSET.toString()), algorithm);
            macGenerator.init(signingKey);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        if (macGenerator == null) {
            return null;
        }

        byte[] dataByte = null;
        try {
            dataByte = data.getBytes(UTF8CHARSET.toString());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        return macGenerator.doFinal(dataByte);
    }

    /**
     * Calculating a message authentication code (MAC) involving a cryptographic hash function in combination with a secret
     * cryptographic key. The result will be represented base64-encoded string.
     *
     * @param algorithm
     *            A cryptographic hash function (such as MD5 or SHA-1)
     * @param key
     *            A secret cryptographic key
     * @param data
     *            The message to be authenticated
     * @return Base64-encoded HMAC String
     */
    public static String hMacBase64Encode(final String algorithm, final String key, final String data) {
        byte[] hmacEncodeBytes = hMacEncode(algorithm, key, data);
        if (hmacEncodeBytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(hmacEncodeBytes);
    }

    /**
     * Calculating a message authentication code (MAC) involving a cryptographic hash function in combination with a secret
     * cryptographic key. The result will be represented hex string.
     *
     * @param algorithm
     *            A cryptographic hash function (such as MD5 or SHA-1)
     * @param key
     *            A secret cryptographic key
     * @param data
     *            The message to be authenticated
     * @return Hex HMAC String
     */
    public static String hMacHexStringEncode(final String algorithm, final String key, final String data) {
        byte[] hmacEncodeBytes = hMacEncode(algorithm, key, data);
        if (hmacEncodeBytes == null) {
            return null;
        }
        return ZaloHexStringUtil.byteArrayToHexString(hmacEncodeBytes);
    }
}
