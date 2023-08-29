package vn.com.ids.myachef.business.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

public class RequestUtils {
    private RequestUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String[] IP_HEADER_CANDIDATES = { //
            "X-Forwarded-For", //
            "Proxy-Client-IP", //
            "WL-Proxy-Client-IP", //
            "HTTP_X_FORWARDED_FOR", //
            "HTTP_X_FORWARDED", //
            "HTTP_X_CLUSTER_CLIENT_IP", //
            "HTTP_CLIENT_IP", //
            "HTTP_FORWARDED_FOR", //
            "HTTP_FORWARDED", //
            "HTTP_VIA", "REMOTE_ADDR" };

    public static String getIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    public static String getServerInfo(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}
