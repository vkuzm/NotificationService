package com.example.project.dto;

import com.example.project.enums.MessageType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageDto {

  private String eventId;
  private String receiver;
  private String sender;
  private String subject;
  private String message;
  private MessageType messageType;
}
