package com.example.project.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageError {

  USER_NOT_FOUND("User not found"),
  SENDER_EMPTY("Sender is empty"),
  SENDER_EMAIL_NOT_VALID("Sender email is not valid: "),
  RECEIVER_EMAIL_NOT_VALID("Receiver email is not valid: "),
  EMAIL_SUBJECT_EMPTY("Email subject is empty"),
  EMAIL_MESSAGE_EMPTY("Email message is empty"),
  MESSAGE_TYPE_INCORRECT("Message type incorrect"),
  NOTIFICATION_EMAIL_NOT_ALLOWED("NOTIFICATION_EMAIL_NOT_ALLOWED"),
  NOTIFICATION_SMS_NOT_ALLOWED("NOTIFICATION_SMS_NOT_ALLOWED"),
  RECEIVER_PHONE_NOT_VALID("Receiver phone number is not valid: ");

  private final String message;
}
