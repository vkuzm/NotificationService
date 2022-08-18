package com.example.project.enums;

import java.util.EnumSet;
import java.util.Objects;

public enum MessageFormat {
  HTML, PLAIN;

  public static boolean exists(String currentFormat) {
    if (Objects.isNull(currentFormat)) {
      return false;
    }

    return EnumSet.allOf(MessageFormat.class)
        .stream()
        .anyMatch((messageFormat -> currentFormat.equals(messageFormat.name())));
  }
}
