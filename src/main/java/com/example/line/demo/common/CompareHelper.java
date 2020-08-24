package com.example.line.demo.common;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by changyenh on 西元2018/1/3.
 */
public class CompareHelper {

  public static boolean isNull(Object... objects) {
    return Arrays.stream(objects).allMatch(Objects::isNull);
  }

  public static boolean nonNull(Object... objects) {
    return Arrays.stream(objects).allMatch(Objects::nonNull);
  }

  public static <T>void ifNull(T object, Consumer<T> ifNull, Consumer<T> nonNull) {
    if (isNull(object)) {
      ifNull.accept(object);
    } else if (nonNull != null) {
      nonNull.accept(object);
    }
  }

  public static <T>void ifNonNull(T object, Consumer<T> nonNull, Consumer<T> ifNull) {
    if (nonNull(object)) {
      nonNull.accept(object);
    } else if (ifNull != null) {
      ifNull.accept(object);
    }
  }

  public static <T>void ifNull(T object, Consumer<T> ifNull) {
    ifNull(object, ifNull, null);
  }

  public static <T>void ifNonNull(T object, Consumer<T> nonNull) {
    ifNonNull(object, nonNull, null);
  }

}
