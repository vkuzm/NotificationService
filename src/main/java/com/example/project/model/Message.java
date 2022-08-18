package com.example.project.model;

import com.example.project.enums.MessageFormat;
import com.example.project.enums.MessageType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
public class Message {

  @Id
  private String id;

  @Indexed(unique = true)
  private String eventId;
  private String receiver;
  private String sender;
  private String subject;
  private String message;
  private MessageFormat messageFormat;
  private MessageType messageType;
  private boolean sent;
}
