package com.example.project.controller;

import com.example.project.dto.ErrorMessageDto;
import com.example.project.dto.MessageDto;
import com.example.project.enums.MessageError;
import com.example.project.enums.MessageFormat;
import com.example.project.enums.MessageType;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import com.example.project.service.MessageSenderService;
import com.example.project.util.PhoneParser;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

  private static final String EMAIL_VALIDATION_FAILED = "Email validation failed: ";
  private static final String SMS_VALIDATION_FAILED = "Sms validation failed: ";

  private final MessageSenderService messageEmailSenderService;
  private final MessageSenderService messageSmsSenderService;
  private final EmailValidator emailValidator;
  private final PhoneNumberUtil phoneNumberUtil;
  private final UserRepository userRepository;

  @PostMapping("/email/send")
  public ResponseEntity<List<ErrorMessageDto>> sendEmail(@RequestBody MessageDto message) {
    List<ErrorMessageDto> errorMessages = new ArrayList<>();
    Optional<User> user = userRepository.findById(message.getUserId());

    if (user.isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.USER_NOT_FOUND)
          .errorMessage(MessageError.USER_NOT_FOUND.getMessage())
          .build();

      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(List.of(error));
    }

    if (isNotificationTypeNotAllowed(user.get(), MessageType.EMAIL)) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.NOTIFICATION_EMAIL_NOT_ALLOWED)
          .errorMessage(MessageError.NOTIFICATION_EMAIL_NOT_ALLOWED.getMessage())
          .build();

      errorMessages.add(error);
    }

    if (!emailValidator.isValid(message.getSender())) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.SENDER_EMAIL_NOT_VALID)
          .errorMessage(MessageError.SENDER_EMAIL_NOT_VALID + message.getSender())
          .build();

      errorMessages.add(error);
    }

    if (!emailValidator.isValid(user.get().getEmail())) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.RECEIVER_EMAIL_NOT_VALID)
          .errorMessage(MessageError.RECEIVER_EMAIL_NOT_VALID + user.get().getEmail())
          .build();

      errorMessages.add(error);
    }

    if (message.getSubject().isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.EMAIL_SUBJECT_EMPTY)
          .errorMessage(MessageError.EMAIL_SUBJECT_EMPTY.getMessage())
          .build();

      errorMessages.add(error);
    }

    if (message.getMessage().isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.EMAIL_MESSAGE_EMPTY)
          .errorMessage(MessageError.EMAIL_MESSAGE_EMPTY.getMessage())
          .build();

      errorMessages.add(error);
    }

    if (!MessageFormat.exists(message.getMessageFormat())) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.MESSAGE_TYPE_INCORRECT)
          .errorMessage(MessageError.MESSAGE_TYPE_INCORRECT.getMessage())
          .build();

      errorMessages.add(error);
    }

    if (!errorMessages.isEmpty()) {
      log.error(EMAIL_VALIDATION_FAILED + errorMessages);

      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(errorMessages);
    }

    messageEmailSenderService.sendEmail(user.get().getEmail(), message);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/sms/send")
  public ResponseEntity<List<ErrorMessageDto>> sendSms(@RequestBody MessageDto message) {
    List<ErrorMessageDto> errorMessages = new ArrayList<>();
    Optional<User> user = userRepository.findById(message.getUserId());

    if (user.isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.USER_NOT_FOUND)
          .errorMessage(MessageError.USER_NOT_FOUND.getMessage())
          .build();

      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(List.of(error));
    }

    if (isNotificationTypeNotAllowed(user.get(), MessageType.SMS)) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.NOTIFICATION_SMS_NOT_ALLOWED)
          .errorMessage(MessageError.NOTIFICATION_SMS_NOT_ALLOWED.getMessage())
          .build();

      errorMessages.add(error);
    }

    if (message.getSender().isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.SENDER_EMPTY)
          .errorMessage(MessageError.SENDER_EMPTY.getMessage())
          .build();

      errorMessages.add(error);
    }

    var phoneNumber = PhoneParser.parse(user.get().getPhoneNumber());
    if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.RECEIVER_PHONE_NOT_VALID)
          .errorMessage(MessageError.RECEIVER_PHONE_NOT_VALID.getMessage())
          .build();

      errorMessages.add(error);
    }

    if (message.getMessage().isEmpty()) {
      var error = ErrorMessageDto.builder()
          .errorCode(MessageError.EMAIL_MESSAGE_EMPTY)
          .errorMessage(MessageError.EMAIL_MESSAGE_EMPTY.getMessage())
          .build();

      errorMessages.add(error);
    }

    if (!errorMessages.isEmpty()) {
      log.error(SMS_VALIDATION_FAILED + errorMessages);

      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(errorMessages);
    }

    messageSmsSenderService.sendSms(user.get().getPhoneNumber(), message);
    return ResponseEntity.ok().build();
  }

  private boolean isNotificationTypeNotAllowed(User user, MessageType messageType) {
    return user.getNotificationSettings().stream()
        .filter(setting -> setting.getType() == messageType)
        .allMatch((setting -> !setting.isStatus()));
  }
}
