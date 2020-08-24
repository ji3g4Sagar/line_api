package com.example.line.demo.common;

import org.json.JSONObject;

/**
 * 由Service封裝好的API回傳類別.
 *
 * @author changyenh
 */
public class Response {

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

    public void Resposne() {
        
    }

    /**
     * 建立包含指定代碼及訊息的回應.
     *
     * @param statusCode 狀態代碼
     * @param message    狀態訊息
     */
    public Response(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = Status.getStatusMessage(statusCode) + " " + message;
    }

    /**
     * 建立包含指定代碼及內容的回應.
     *
     * @param statusCode 狀態代碼
     * @param payLoad    回應內容
     */
    public Response(Integer statusCode, Object payLoad) {
        this.statusCode = statusCode;
        this.message = Status.getStatusMessage(statusCode);
        this.payLoad = payLoad;
    }

    /**
     * 建構子.
     *
     * @param statusCode 狀態代碼
     */
    public Response(Integer statusCode) {
        this.statusCode = statusCode;
        this.message = Status.getStatusMessage(statusCode);
    }

    /**
     * 建立包含Payload的OK Response.
     *
     * @param payLoad payload
     * @return OK Response
     */
    public static Response ok(Object payLoad) {
        return new Response(Status.OK, payLoad);
    }

    /**
     * 建立OK Response.
     *
     * @return OK {@link Response}
     */
    public static Response ok() {
        return ok(null);
    }

    /**
     * 建立包含Payload的FAILED Response.
     *
     * @param payLoad payload
     * @return FAILED {@link Response}
     */
    public static Response failed(Object payLoad) {
        return new Response(Status.FAILED, payLoad);
    }

    /**
     * 建立FAILED Response.
     *
     * @return OK {@link Response}
     */
    public static Response failed() {
        return failed(null);
    }

    /**
     * 建立OK Response.
     *
     * @return OK {@link Response}
     */
    public static Response empty(Integer statusCode) {
        return new Response(statusCode);
    }

    /**
     * 建立OK Response.
     *
     * @return OK {@link Response}
     */
    public static Response create(Integer statusCode) {
        return new Response(statusCode);
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