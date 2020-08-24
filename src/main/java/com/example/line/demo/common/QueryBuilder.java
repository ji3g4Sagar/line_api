package com.example.line.demo.common;

import lombok.Data;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.line.demo.common.CompareHelper.isNull;
import static com.example.line.demo.common.CompareHelper.nonNull;


/**
 * Created by changyenh on 西元2017/12/8.
 */
public class QueryBuilder {

  @Data
  public static class Parameter {
    public static int EQUAL = 0;
    public static int GREATER = 1;
    public static int LESS = -1;
    private String key;
    private Object value;
    private int compare;

    public Parameter(String key, Object value) {
      this(key, value, EQUAL);
    }

    public Parameter(String key, Object value, int compare) {
      this.key = key;
      this.value = value;
      this.compare = compare;
    }
  }

  public static void buildOrderQuery(
      Map<String, Object> paramMap, QueryParam queryParam, StringBuilder sbSql, String tableName) {
    if (queryParam != null) {
      if (queryParam.getOrder() != null && queryParam.getSort() != null) {
        String uiorder = queryParam.getOrder();
        String uiSort = queryParam.getSort();
        // Replace key,key2,key3 to key,T.key2,T.key3
        uiorder = uiorder.replaceAll(",", "," + tableName);
        sbSql
            .append(" Order By ")
            .append(tableName)
            .append(uiorder)
            .append(" ")
            .append(uiSort)
            .append(" ");
      } else {
        Boolean isNewFirst = Optional.of(queryParam.getIsNewFirst()).orElse(true);
        Boolean isNew = queryParam.getIsNew();
        Boolean isOrderCreateTimeNotNull = queryParam.getOrderKey() != null;
        if (isNew != null && isOrderCreateTimeNotNull) {
          paramMap.put("key", queryParam.getOrderKey());
          if (isNewFirst && isNew) {
            sbSql.append(" AND key < :key ");
          } else if (isNewFirst && !isNew) {
            sbSql.append(" AND key > :key ");
          } else if (!isNewFirst && isNew) {
            sbSql.append(" AND key > :key ");
          } else if (!isNewFirst && !isNew) {
            sbSql.append(" AND key < :key ");
          }
        }
        if (isNewFirst) {
          sbSql.append(" Order By ").append(tableName).append("key ").append("DESC");
        } else {
          sbSql.append(" Order By ").append(tableName).append("key ").append("ASC");
        }
      }
    }
  }

  /**
   * 建立SQL查詢及排序條件.
   * 根據參數決定以游標、時間或其他條件做查詢及排序.
   *
   * @param paramMap   SQL條件參數存放物件
   * @param queryParam 查詢條件參數
   * @param sbSql      建立SQL的String Builder
   */
  public static void buildQuery(Map<String, Object> paramMap, QueryParam queryParam, StringBuilder sbSql) {
    buildQuery(paramMap, queryParam, sbSql, true);
  }

  /**
   * 建立SQL查詢及排序條件.
   * 根據參數決定以游標、時間或其他條件做查詢及排序.
   *
   * @param paramMap   SQL條件參數存放物件
   * @param queryParam 查詢條件參數
   * @param sbSql      建立SQL的String Builder
   */
  public static void buildQuery(Map<String, Object> paramMap,
                                QueryParam queryParam,
                                StringBuilder sbSql,
                                boolean order) {
    //建立新的QueryParam以確保不影響原本的
    QueryParam param = new QueryParam();
    if (queryParam != null) {
      param.setSort(queryParam.getSort());
      param.setOrder(queryParam.getOrder());
      param.setPrevious(queryParam.getPrevious());
      param.setCursor(queryParam.getCursor());
      param.setSince(queryParam.getSince());
      param.setUntil(queryParam.getUntil());
    }
    //分成兩個系統進行查詢
    List<Object> orderQueryParams = Arrays.asList(param.getSort(),
        param.getOrder(), param.getSince(), param.getUntil(), param.getLimit());
    List<Object> pagingQueryParams = Arrays.asList(
        param.getCursor(), param.getPrevious(), param.getLimit());
    //若無條件則預設排序條件為以時間降序排列 (最新的在最前面)
    if (orderQueryParams.stream().allMatch(CompareHelper::isNull)
        && pagingQueryParams.stream().allMatch(CompareHelper::isNull)) {
      param.setSort(QueryParam.UPDATE_TIME);
      param.setOrder(QueryParam.DESC);
    }
    //
    if (nonNull(param.getBefore()) || nonNull(param.getAfter())) {
      //Default Order
      if (param.getOrder() == null) {
        param.setOrder(QueryParam.DESC);
      }
      //Get Previous/Next page
      if (param.getBefore() >= 0L) {
        sbSql.append(" AND key < :before");
        param.setOrder(QueryParam.DESC);
        paramMap.put("before", param.getBefore());
      }
      if (param.getAfter() >= 0L) {
        sbSql.append(" AND key > :after");
        param.setOrder(QueryParam.ASC);
        paramMap.put("after", param.getAfter());
      }
      param.setSort(QueryParam.KEY);
    } else {
      boolean canReplaceSort = isNull(param.getSort()) || !param.getSort().equals(QueryParam.CREATE_TIME);
      if (nonNull(param.getSince())) {
        if (canReplaceSort) {
          param.setSort(QueryParam.UPDATE_TIME);
        }
        sbSql.append(" AND ").append(param.getSort()).append(" > :sinceTime");
        paramMap.put("sinceTime", param.getSince());
        param.setOrder(QueryParam.ASC);
      }
      if (nonNull(param.getUntil())) {
        if (canReplaceSort) {
          param.setSort(QueryParam.UPDATE_TIME);
        }
        sbSql.append(" AND ").append(param.getSort()).append(" < :untilTime");
        paramMap.put("untilTime", param.getUntil());
        param.setOrder(QueryParam.DESC);
      }
    }
    if (order) {
      if (nonNull(param.getSort())) {
        sbSql.append(" ORDER BY ").append(param.getSort());
      }
      if (nonNull(param.getOrder())) {
        sbSql.append(" ").append(param.getOrder());
      }
    }
  }

  public static void buildQueryOrder(QueryParam queryParam, StringBuilder sbSql) {
    if (nonNull(queryParam.getSort())) {
      sbSql.append(" ORDER BY ").append(queryParam.getSort());
    }
    if (nonNull(queryParam.getOrder())) {
      sbSql.append(" ").append(queryParam.getOrder());
    }
  }

  public static void setQueryLimitOffset(QueryParam queryParam, Query query) {
    if (queryParam != null) {
      if (nonNull(queryParam.getLimit())) {
        query.setMaxResults(queryParam.getLimit());
      }
      if (nonNull(queryParam.getOffset())) {
        query.setFirstResult(queryParam.getOffset());
      }
    }
  }

  public static void setParameters(Query query, Map<String, Object> paramMap) {
    paramMap.keySet().forEach(
        (parameterName) -> query.setParameter(parameterName, paramMap.get(parameterName))
    );
  }

  public static void setInQueryConditions(Map<String, Object> paramMap, List<String> criList,
                                          AbstractMap.SimpleEntry<String, List<?>>... pairs) {
    for (int i = 0; i < pairs.length; i++) {
      AbstractMap.SimpleEntry pair = pairs[i];
      if (nonNull(pair.getValue())) {
        criList.add(pair.getKey() + " IN (:inVar" + i + ")");
        paramMap.put("inVar" + i, pair.getValue());
      }
    }
  }

  public static void setGreaterThanConditions(Map<String, Object> paramMap, List<String> criList,
                                              AbstractMap.SimpleEntry<String, Object>... pairs) {
    for (int i = 0; i < pairs.length; i++) {
      AbstractMap.SimpleEntry pair = pairs[i];
      if (nonNull(pair.getValue())) {
        criList.add(pair.getKey() + " >= :gtVar" + i);
        paramMap.put("gtVar" + i, pair.getValue());
      }
    }
  }

  public static void setLessThanConditions(Map<String, Object> paramMap, List<String> criList,
                                              AbstractMap.SimpleEntry<String, Object>... pairs) {
    for (int i = 0; i < pairs.length; i++) {
      AbstractMap.SimpleEntry pair = pairs[i];
      if (nonNull(pair.getValue())) {
        criList.add(pair.getKey() + " < :lsVar" + i);
        paramMap.put("lsVar" + i, pair.getValue());
      }
    }
  }

  public static void setQueryConditions(Map<String, Object> paramMap, List<String> criList,
                                        AbstractMap.SimpleEntry<String, Object>... pairs) {
    for (int i = 0; i < pairs.length; i++) {
      AbstractMap.SimpleEntry pair = pairs[i];
      if (nonNull(pair.getValue())) {
        criList.add(pair.getKey() + " = :var" + i);
        paramMap.put("var" + i, pair.getValue());
      }
    }
    /*Arrays.stream(pairs).filter(pair -> nonNull(pair.getValue())).forEach(pair -> {
      criList.add(pair.getKey() + " = :" + pair.getKey());
      paramMap.put(pair.getKey(), pair.getValue());
    });*/
  }

  public static Predicate[] getQueryConditions(CriteriaBuilder builder, AbstractMap.SimpleEntry<Path, Object>... pairs) {
    return getQueryConditions(builder, Arrays.asList(pairs));
  }

  public static Predicate[] getQueryConditions(CriteriaBuilder builder, List<AbstractMap.SimpleEntry<Path, Object>> pairs) {
    return (Predicate[]) pairs.stream().map(pair ->
        builder.equal(pair.getKey(), pair.getValue())
    ).toArray();
  }

  public static Predicate[] getQueryConditions(List<Predicate> predicates) {
    return predicates.toArray(new Predicate[]{});
  }

  public static Predicate[] getQueryConditions(Predicate... predicates) {
    return predicates;
  }

  public static List<Predicate> buildConditionList(CriteriaBuilder builder, AbstractMap.SimpleEntry<Path, Object>... pairs) {
    return Arrays.stream(pairs).filter(pair ->
        nonNull(pair.getValue())
    ).map(pair ->
        builder.equal(pair.getKey(), pair.getValue())
    ).collect(Collectors.toList());
  }

  public static List<Selection> buildSelectionList(Root<?> root, List<String> fields) {
    return fields.stream().map(field ->
        root.get(field).alias(field)
    ).collect(Collectors.toList());
  }

  public static StringBuilder getBasicSql(String tableName) {
    return getBasicSql(null, tableName);
  }

  public static StringBuilder getBasicSql(List<String> fields, String tableName) {
    StringBuilder sbSql = new StringBuilder();
    if (fields != null && fields.size() > 0) {
      sbSql.append("SELECT NEW MAP(");
      sbSql.append(fields.stream().map(field ->
          field + " as " + field
      ).collect(Collectors.joining(",")));
      sbSql.append(") ");
    }
    sbSql.append("FROM ").append(tableName).append(" WHERE logicalDelete = 0");
    return sbSql;
  }

}
