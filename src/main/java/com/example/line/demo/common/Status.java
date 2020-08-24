package com.example.line.demo.common;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * 紀錄Service層邏輯判斷狀態代碼，以及對應的Message
 *
 * @author TimChen
 * @version 0.1.0
 */
public class Status {

    public static final int OK = 1;
    public static final int FAILED = -1;
    public static final int EXIST_USER = -6;
    public static final int NON_EXISTENT_USER = -14;
    public static final int NON_EXISTENT_CASE = -15;
    public static final int MISSING_PARAMETER = -36;
    public static final int LINE_NOT_BIND = -40;

    private static final Map<Integer, String> STATUS_MAP = new HashMap<>();

    static {
        STATUS_MAP.put(1, "OK");
        STATUS_MAP.put(-1, "未知的錯誤");
        STATUS_MAP.put(-6, "已存在的使用者");
        STATUS_MAP.put(-14, "不存在的使用者");
        STATUS_MAP.put(-15, "不存在的個案");
        STATUS_MAP.put(-36, "缺少必要參數");
        STATUS_MAP.put(-40, "LINE尚未綁定");
    }

    /**
     * 以Status Code取得對應Message.
     *
     * @param statusCode 邏輯狀態代碼
     * @return String
     */
    public static String getStatusMessage(int statusCode) {
        return STATUS_MAP.get(statusCode);
    }

    /**
     * 設定JSONObject Response內StatusCode Message.
     *
     * @param json json
     * @param status status
     * @return {@link JSONObject}
     */
    public static JSONObject setResponseJson(JSONObject json, int status) {
        json.put("statusCode", status);
        json.put("message", getStatusMessage(status));
        return json;
  }
}