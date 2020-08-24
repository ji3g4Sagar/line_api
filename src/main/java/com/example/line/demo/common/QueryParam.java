/*
 * QueryParam
 * Author : Sian
 * QueryParam Vo
 */

package com.example.line.demo.common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 2017/06/30 - Add UserInfoVo.
 */
@Data
public class QueryParam {

  public static final String ASC = "ASC";
  public static final String DESC = "DESC";
  public static final String KEY = "key";
  public static final String UPDATE_TIME = "updateTime";
  public static final String CREATE_TIME = "createTime";

  /**
   * number.
   */
  private Integer number = 0;

  /**
   * rows.
   */
  private Integer rows = 0;

  private Boolean isNewFirst = true; // true = DESC ,false = ASC

  private Boolean isNew = null; // 拉取清單往下或者是往前拉

  private Long orderKey = null;

  //新的排序

  /**
   * Cursor(vKey).
   */
  private Long cursor = null;

  /**
   * Before Cursor(vKey).
   */
  private Long before = null;

  /**
   * After Cursor(vKey).
   */
  private Long after = null;

  /**
   * 是否為上一頁.
   */
  private Boolean previous = false;

  /**
   * 指定時間之後.
   */
  private LocalDateTime since = null;

  /**
   * 指定時間之前.
   */
  private LocalDateTime until = null;

  /**
   * 資料筆數上限.
   */
  private Integer limit = null;

  /**
   * 排序的欄位名稱（複選）.
   */
  private String sort = null;

  /**
   * ASC/DESC.
   */
  private String order = null;

  /**
   * 偏移.
   */
  private Integer offset = null;

  /**
   * fields.
   */
  private List<String> fields = null;

  /**
   * Base Url, For Paging Useage.
   */
  private String baseUrl = null;

  public void setFields(List<String> fields) {
    this.fields = new ArrayList<>(fields);
  }

  /**
   * 若有指定欄位則檢查取得欄位中是否包含排序的欄位.
   * 若不包含則補上並回傳結果.
   *
   * @return 有補上參數回傳true, 不需要則回傳false.
   */
  public boolean checkFields() {
    if (sort != null && (sort.equals(UPDATE_TIME) || sort.equals(CREATE_TIME))) {
      addFields(sort);
    } else if (since != null || until != null) {
      addFields(UPDATE_TIME);
    } else {
      return false;
    }
    return true;
  }

  private void addFields(String... fields) {
    if (this.fields == null) {
      this.fields = new ArrayList<>();
    }
    Arrays.stream(fields).forEach(field -> {
          if (!this.fields.contains(field)) {
            this.fields.add(field);
          }
        }
    );
  }

  /**
   * 建立基本查詢參數.
   * 依照更新時間降序排列.
   *
   * @return 基本查詢參數
   */
  public static QueryParam simpleQuery() {
    QueryParam queryParam = new QueryParam();
    queryParam.setSort(UPDATE_TIME);
    queryParam.setOrder(DESC);
    return queryParam;
  }

  /**
   * getOrderByString.
   *
   * @return orderString
   */
  public final String getOrderByString() {
    return this.getOrderByString(false);
  }

  /**
   * getOrderByString.
   *
   * @param quoteColumn quote
   * @return orderString
   */
  public final String getOrderByString(final boolean quoteColumn) {
    if (StringUtils.isBlank(this.sort)) {
      return "";
    }
    String quoteString = "";
    if (quoteColumn) {
      quoteString = "\"";
    }
    String srt = this.sort;
    String ord = this.order;
    if (StringUtils.isBlank(ord)) {
      ord = "ASC";
    }
    String[] columns = srt.split(",");
    String[] orders = ord.split(",");
    if (columns.length == 1) {
      return " ORDER BY "
          + quoteString
          + StringUtils.trim(srt)
          + quoteString
          + " "
          + StringUtils.trim(ord);
    } else {
      List<String> columnList = Arrays.asList(columns);
      List<String> orderList = new ArrayList<>();
      orderList.addAll(Arrays.asList(orders));
      if (columnList.size() > orderList.size()) {
        while (columnList.size() != orderList.size()) {
          orderList.add("ASC");
        }
      }
      StringBuilder builder = new StringBuilder();
      for (int i = 0, size = columnList.size(); i < size; i++) {
        builder
            .append(", ")
            .append(quoteString)
            .append(StringUtils.trim(columnList.get(i)))
            .append(quoteString)
            .append(" ")
            .append(StringUtils.trim(orderList.get(i)));
      }
      return " ORDER BY " + builder.substring(1);
    }
  }
}
