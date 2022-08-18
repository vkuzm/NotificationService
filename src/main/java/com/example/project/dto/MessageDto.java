package com.example.project.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageDto {

  private String eventId;
  private String userId;
  private String receiver;
  private String sender;
  private String subject;
  private String message;
  private String messageFormat;
}
