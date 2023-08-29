package vn.com.ids.myachef.business.onesignal;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.OnesignalConfiguration;

@Service
@Slf4j
public class OnesignalPushNotification implements OnesignalPushNotificationService {

    private static final String UTF_8 = "UTF-8";

    @Autowired
    private OnesignalConfiguration onesignalConfiguration;

    public String mountResponseRequest(HttpURLConnection con, int httpResponse) throws IOException {
        String jsonResponse;
        if (httpResponse >= HttpURLConnection.HTTP_OK && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
            Scanner scanner = new Scanner(con.getInputStream(), UTF_8);
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        } else {
            Scanner scanner = new Scanner(con.getErrorStream(), UTF_8);
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        return jsonResponse;
    }

    private HttpURLConnection getConnection() throws IOException {
        URL url = new URL(onesignalConfiguration.getOnesignalZaloAssistantPushUrl());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);

        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestMethod("POST");
        return con;
    }

    private String buildJsonBodyForAppPush(List<String> pushTokens, String appUrl, String message, String headings) {
        return "{" //
                + "\"app_id\": \"" + onesignalConfiguration.getOneSignalZaloAssistantAppId() + "\"," //
                + "\"include_player_ids\": [" + buildIncludedPlayerIds(pushTokens) + "]," //
                + "\"headings\":{\"en\":\"" + headings + "\"}," //
                + "\"app_url\":\"" + appUrl + "\"," //
                + "\"contents\": {\"en\": \"" + message + "\"}" //
                + "}";
    }

    private String buildIncludedPlayerIds(List<String> pushTokens) {
        StringBuilder includedPlayerIdsStr = new StringBuilder();
        for (String deviceToken : pushTokens) {
            includedPlayerIdsStr.append("\"") //
                    .append(deviceToken) //
                    .append("\"");
            if (pushTokens.indexOf(deviceToken) != (pushTokens.size() - 1)) {
                includedPlayerIdsStr.append(",");
            }
        }
        return includedPlayerIdsStr.toString();
    }

    @Override
    public String pushNotification(List<String> pushTokens, String appUrl, String message, String headings) {
        String jsonResponse = null;
        try {
            HttpURLConnection con = getConnection();
            String strJsonBody = buildJsonBodyForAppPush(pushTokens, appUrl, message, headings);

            byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(sendBytes.length);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            jsonResponse = mountResponseRequest(con, httpResponse);
            log.info(jsonResponse);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return jsonResponse;
    }
}
