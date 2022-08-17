package com.example.project.enums;

import java.util.EnumSet;

public enum MessageType {
  HTML, PLAIN;

  public static boolean exists(MessageType currentType) {
    return EnumSet.allOf(MessageType.class)
        .stream()
        .anyMatch((messageType -> currentType == messageType));
  }
}
