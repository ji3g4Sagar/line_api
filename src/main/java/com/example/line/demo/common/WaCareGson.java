package com.example.line.demo.common;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * 為了自定義Gson序列化方法所以使用GsonBuilder實作一個新的Gson物件(WaCareGson)。 目前是因為遇到Java 8 LocalDateTime
 * 沒辦法正常的被Gson序列化以及反序列化，所以加入LocalDateTime轉換的類別。
 *
 * @author TimChen
 */
public class WaCareGson {
  private static final Gson GSON =
      new GsonBuilder()
          .setPrettyPrinting()
          .create();

  /**
   * 取得WaCareGson 宣告好的gson 物件
   *
   * @return
   */
  public static Gson getWacareGson() {
    return GSON;
  }
}
