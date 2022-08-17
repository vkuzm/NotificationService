package com.example.project.controller;

import com.example.project.dto.ErrorMessageDto;
import com.example.project.dto.MessageDto;
import com.example.project.enums.MessageType;
import com.example.project.service.MessageSenderService;
import com.example.project.util.PhoneParser;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Log4j2
public class NotificationController {

  private static final String SENDER_EMAIL_NOT_VALID_CODE = "SENDER_EMAIL_NOT_VALID";
  private static final String RECEIVER_EMAIL_NOT_VALID_CODE = "RECEIVER_EMAIL_NOT_VALID";
  private static final String RECEIVER_PHONE_NOT_VALID_CODE = "RECEIVER_PHONE_NOT_VALID";
  private static final String SENDER_EMPTY_CODE = "SENDER_EMPTY";
  private static final String EMAIL_SUBJECT_EMPTY_CODE = "EMAIL_SUBJECT_EMPTY";
  private static final String EMAIL_MESSAGE_EMPTY_CODE = "EMAIL_MESSAGE_EMPTY";
  private static final String MESSAGE_TYPE_INCORRECT_CODE = "MESSAGE_TYPE_INCORRECT";
  private static final String SENDER_EMAIL_NOT_VALID_MESSAGE = "Sender email is not valid: ";
  private static final String RECEIVER_EMAIL_NOT_VALID_MESSAGE = "Receiver email is not valid: ";
  private static final String SENDER_EMPTY_MESSAGE = "Sender is empty: ";
  private static final String RECEIVER_PHONE_NOT_VALID_MESSAGE = "Receiver phone number is not valid: ";
  private static final String EMAIL_SUBJECT_EMPTY_MESSAGE = "Email subject is empty";
  private static final String EMAIL_MESSAGE_EMPTY_MESSAGE = "Email message is empty";
  private static final String MESSAGE_TYPE_INCORRECT_MESSAGE = "Message type incorrect";
  private static final String EMAIL_VALIDATION_FAILED = "Email validation failed: ";
  private static final String SMS_VALIDATION_FAILED = "Sms validation failed: ";

  private final MessageSenderService messageEmailSenderService;
  private final MessageSenderService messageSmsSenderService;
  private final EmailValidator emailValidator;
  private final PhoneNumberUtil phoneNumberUtil;

  @PostMapping("/email/send")
  public ResponseEntity<List<ErrorMessageDto>> sendEmail(@RequestBody MessageDto message) {
    List<ErrorMessageDto> errorMessages = new ArrayList<>();

    if (!emailValidator.isValid(message.getSender())) {
      var error = ErrorMessageDto.builder()
          .errorCode(SENDER_EMAIL_NOT_VALID_CODE)
          .errorMessage(SENDER_EMAIL_NOT_VALID_MESSAGE + message.getSender())
          .build();

      errorMessages.add(error);
    }

    if (!emailValidator.isValid(message.getReceiver())) {
      var error = ErrorMessageDto.builder()
          .errorCode(RECEIVER_EMAIL_NOT_VALID_CODE)
          .errorMessage(RECEIVER_EMAIL_NOT_VALID_MESSAGE + message.getReceiver())
          .build();

      errorMessages.add(error);
    }

    if (message.getSubject().isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(EMAIL_SUBJECT_EMPTY_CODE)
          .errorMessage(EMAIL_SUBJECT_EMPTY_MESSAGE)
          .build();

      errorMessages.add(error);
    }

    if (message.getMessage().isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(EMAIL_MESSAGE_EMPTY_CODE)
          .errorMessage(EMAIL_MESSAGE_EMPTY_MESSAGE)
          .build();

      errorMessages.add(error);
    }

    if (!MessageType.exists(message.getMessageType())) {
      var error = ErrorMessageDto.builder()
          .errorCode(MESSAGE_TYPE_INCORRECT_CODE)
          .errorMessage(MESSAGE_TYPE_INCORRECT_MESSAGE)
          .build();

      errorMessages.add(error);
    }

    if (!errorMessages.isEmpty()) {
      log.error(EMAIL_VALIDATION_FAILED + errorMessages);

      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(errorMessages);
    }

    messageEmailSenderService.send(message);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/sms/send")
  public ResponseEntity<List<ErrorMessageDto>> sendSms(@RequestBody MessageDto message) {
    List<ErrorMessageDto> errorMessages = new ArrayList<>();

    if (message.getSender().isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(SENDER_EMPTY_CODE)
          .errorMessage(SENDER_EMPTY_MESSAGE + message.getSender())
          .build();

      errorMessages.add(error);
    }

    var phoneNumber = PhoneParser.parse(message.getReceiver());
    if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
      var error = ErrorMessageDto.builder()
          .errorCode(RECEIVER_PHONE_NOT_VALID_CODE)
          .errorMessage(RECEIVER_PHONE_NOT_VALID_MESSAGE + message.getReceiver())
          .build();

      errorMessages.add(error);
    }

    if (message.getMessage().isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(EMAIL_MESSAGE_EMPTY_CODE)
          .errorMessage(EMAIL_MESSAGE_EMPTY_MESSAGE)
          .build();

      errorMessages.add(error);
    }

    if (!errorMessages.isEmpty()) {
      log.error(SMS_VALIDATION_FAILED + errorMessages);

      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(errorMessages);
    }

    messageSmsSenderService.send(message);
    return ResponseEntity.ok().build();
  }
}
