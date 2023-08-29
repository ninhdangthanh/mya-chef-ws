package vn.com.ids.myachef.business.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class ValidationUtils {

    private static final String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String WORKSPACE_PATTERN = "[^a-z0-9-]";
    private static final String IDENTITY_CARD = "[0-9]{9,12}$";

    private static final String PHONE_NUMBER_PATTERN = "((^(\\+84|84|0|0084){1})(3|5|7|8|9))+([0-9]{8})$";
    private static final String LANDLINE_PHONE_02 = "((^(\\+84|84|0|0084){1})(2)[0-9]{1,2})+([0-9]{8})$";
    private static final String LANDLINE_PHONE_1900 = "((^(1900){1}))+([0-9]{4}|[0-9]{6})$";

    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean validateCompanyHotLineNumber(String number) {
        boolean isAccept = false;
        if (number == null || number.isEmpty()) {
            return isAccept;
        }

        Pattern phoneNumberPattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher phoneNumberMatcher = phoneNumberPattern.matcher(number);

        Pattern landLinePhonePattern1990 = Pattern.compile(LANDLINE_PHONE_1900);
        Matcher landLinePhoneMatcher1990 = landLinePhonePattern1990.matcher(number);

        Pattern landLinePhonePattern02 = Pattern.compile(LANDLINE_PHONE_02);
        Matcher landLinePhoneMatcher02 = landLinePhonePattern02.matcher(number);

        if (phoneNumberMatcher.find() || landLinePhoneMatcher1990.find() || landLinePhoneMatcher02.find()) {
            isAccept = true;
        }

        return isAccept;
    }

    public static boolean validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateWorkspace(String workspace) {
        if (!StringUtils.hasText(workspace)) {
            return true;
        }
        Pattern pattern = Pattern.compile(WORKSPACE_PATTERN);
        Matcher matcher = pattern.matcher(workspace);
        return matcher.find();
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            return false;
        }
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.find();
    }

    public static boolean validateIdentityCard(String identityCard) {
        if (!StringUtils.hasText(identityCard)) {
            return false;
        }

        if (identityCard.length() != 9 && identityCard.length() != 12) {
            return false;
        }

        Pattern pattern = Pattern.compile(IDENTITY_CARD);
        Matcher matcher = pattern.matcher(identityCard);
        return matcher.find();
    }
}
