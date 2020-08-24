package com.example.line.demo.common;


import com.google.gson.Gson;
import lombok.Data;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 由Service封裝好的API回傳類別.
 *
 * @author changyenh
 */
@Data
public class WaCareResponse {

  private static final Gson gson = WaCareGson.getWacareGson();

  /**
   * 狀態代碼.
   */
  private Integer statusCode = 0;

  /**
   * 狀態代碼描述.
   */
  private String message = "";

  /**
   * 回傳附加的資料.
   */
  private Object payLoad = null;

  public WaCareResponse() {
  }

  /**
   * 設定statusCode並連動取得對應statusCode的message設定.
   *
   * @param statusCode 狀態代碼
   */
  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
    this.message = Status.getStatusMessage(statusCode);
  }

  /**
   * 取得狀態代碼.
   *
   * @return 狀態代碼 {@link Status}
   */
  public Integer getStatusCode() {
    return this.statusCode;
  }

  /**
   * 建立包含指定代碼及訊息的回應.
   *
   * @param statusCode 狀態代碼
   * @param message    狀態訊息
   */
  public WaCareResponse(Integer statusCode, String message) {
    this.statusCode = statusCode;
    this.message = Status.getStatusMessage(statusCode) + " " + message;
  }

  /**
   * 建立包含指定代碼及內容的回應.
   *
   * @param statusCode 狀態代碼
   * @param payLoad    回應內容
   */
  public WaCareResponse(Integer statusCode, Object payLoad) {
    this.statusCode = statusCode;
    this.message = Status.getStatusMessage(statusCode);
    this.payLoad = payLoad;
  }

  /**
   * 建構子.
   *
   * @param statusCode 狀態代碼
   */
  public WaCareResponse(Integer statusCode) {
    this.statusCode = statusCode;
    this.message = Status.getStatusMessage(statusCode);
  }

  /**
   * 建立包含Payload的OK WaCareResponse.
   *
   * @param payLoad payload
   * @return OK WaCareResponse
   */
  public static WaCareResponse ok(Object payLoad) {
    return new WaCareResponse(Status.OK, payLoad);
  }

  /**
   * 建立OK WaCareResponse.
   *
   * @return OK {@link WaCareResponse}
   */
  public static WaCareResponse ok() {
    return ok(null);
  }

  /**
   * 建立包含Payload的FAILED WaCareResponse.
   *
   * @param payLoad payload
   * @return FAILED {@link WaCareResponse}
   */
  public static WaCareResponse failed(Object payLoad) {
    return new WaCareResponse(Status.FAILED, payLoad);
  }

  /**
   * 建立FAILED WaCareResponse.
   *
   * @return OK {@link WaCareResponse}
   */
  public static WaCareResponse failed() {
    return failed(null);
  }

  /**
   * 建立OK WaCareResponse.
   *
   * @return OK {@link WaCareResponse}
   */
  public static WaCareResponse empty(Integer statusCode) {
    return new WaCareResponse(statusCode);
  }

  /**
   * 建立OK WaCareResponse.
   *
   * @return OK {@link WaCareResponse}
   */
  public static WaCareResponse create(Integer statusCode) {
    return new WaCareResponse(statusCode);
  }

  @Override
  public String toString() {
    return new JSONObject()
        .put("statusCode", statusCode)
        .put("message", message)
        .put("payLoad", JSONObject.wrap(payLoad))
        .toString(4);
  }
}
