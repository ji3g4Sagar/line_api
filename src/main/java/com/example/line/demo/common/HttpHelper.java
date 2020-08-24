package com.example.line.demo.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

/**
 *
 * @author MikeyJou
 */
public class HttpHelper {

    private String sessionId = "";

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String id) {
        sessionId = id;
    }

    private String getFormDataString(JSONObject params) {
        String result = "";
        boolean first = true;
        Iterator<?> keys = params.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (params.get(key) instanceof String) {
                if (first) {
                    first = false;
                } else {
                    result = result + "&";
                }
                result = result + key;
                result = result + "=";
                result = result + params.getString(key).replace("+", "%2B");
            }
        }
        return result.toString();
    }

    public String getResponseBody(String targetUrl, String accessToken, JSONObject params) throws Exception {
        System.out.println(targetUrl);
        URL url = new URL(targetUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        if(accessToken != null)
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        
        if (!StringUtils.isEmpty(sessionId)) {
            System.out.println("sessionId: " + sessionId);
            conn.addRequestProperty("Cookie", sessionId);
        }
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects(true);
        
        StringBuilder sb = new StringBuilder();

        if (params != null) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(params.toString());
            bw.flush();
            bw.close();
        }
        int status = conn.getResponseCode();
        System.out.println("Status code: " + String.valueOf(status));
        if (200 <= status && status <= 299) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }

        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookiesHeader = headerFields.get("Set-Cookie");
        if (cookiesHeader != null) {
            System.out.println("COOKIE");
            for (String cookie : cookiesHeader) {
                System.out.println(cookie);
                if (StringUtils.isEmpty(sessionId) && cookie.toString().contains("ASP.NET_SessionId")) {
                    sessionId = cookie.toString();
                }
            }
        }
        
        return sb.toString();
    }
}
