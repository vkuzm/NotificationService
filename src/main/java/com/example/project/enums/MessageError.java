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
  MESSAGE_EMPTY("Message is empty"),
  SUBJECT_EMPTY("Subject is empty"),
  MESSAGE_TYPE_INCORRECT("Message type incorrect"),
  NOTIFICATION_EMAIL_NOT_ALLOWED("Notification email is not allowed"),
  NOTIFICATION_SMS_NOT_ALLOWED("Notification sms is not allowed"),
  NOTIFICATION_APP_NOT_ALLOWED("Notification app is not allowed"),
  NOTIFICATION_TOKEN_EMPTY("Notification token is empty"),
  RECEIVER_PHONE_NOT_VALID("Receiver phone number is not valid: ");

  private final String message;
}
