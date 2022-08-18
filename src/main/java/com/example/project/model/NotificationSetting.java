package com.example.project.model;

import com.example.project.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationSetting {

  private MessageType type;
  private boolean status;
}
