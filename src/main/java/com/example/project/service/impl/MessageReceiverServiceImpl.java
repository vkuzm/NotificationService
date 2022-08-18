package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.enums.MessageFormat;
import com.example.project.enums.MessageType;
import com.example.project.model.Message;
import com.example.project.repository.MessageRepository;
import com.example.project.service.EmailSenderService;
import com.example.project.service.FirebaseMessagingService;
import com.example.project.service.MessageReceiverService;
import com.example.project.service.SmsSenderService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessageReceiverServiceImpl implements MessageReceiverService {

  private static final String FOUND_DUPLICATED_EMAIL = "Found duplicated email: ";
  private static final String FOUND_DUPLICATED_SMS = "Found duplicated sms: ";
  private static final String FOUND_DUPLICATED_NOTIFICATION = "Found duplicated app notification: ";

  private final EmailSenderService emailSenderService;
  private final SmsSenderService smsSenderService;
  private final FirebaseMessagingService firebaseMessagingService;
  private final MessageRepository messageRepository;

  @Override
  @RabbitListener(queues = "${amqp.notification.queues.email}")
  public void receiveEmail(MessageDto message) {
    var existedMessage = getMessageByEventId(message);

    if (isMessageSent(existedMessage)) {
      log.error(FOUND_DUPLICATED_EMAIL + message);
      return;
    }

    if (isMessageEmpty(existedMessage)) {
      var createdEmail = Message.builder()
          .eventId(message.getEventId())
          .receiver(message.getReceiver())
          .sender(message.getSender())
          .subject(message.getSubject())
          .message(message.getMessage())
          .messageFormat(MessageFormat.valueOf(message.getMessageFormat()))
          .messageType(MessageType.EMAIL)
          .build();

      messageRepository.save(createdEmail);
    }

    if (isMessageNotSent(existedMessage) && emailSenderService.send(message)) {
      updateMessageAsSent(message);
    }
  }

  @Override
  @RabbitListener(queues = "${amqp.notification.queues.sms}")
  public void receiveSms(MessageDto message) {
    var existedMessage = getMessageByEventId(message);

    if (isMessageSent(existedMessage)) {
      log.error(FOUND_DUPLICATED_SMS + message);
      return;
    }

    if (isMessageEmpty(existedMessage)) {
      var createdSms = Message.builder()
          .eventId(message.getEventId())
          .receiver(message.getReceiver())
          .sender(message.getSender())
          .message(message.getMessage())
          .messageType(MessageType.SMS)
          .build();

      messageRepository.save(createdSms);
    }

    if (isMessageNotSent(existedMessage) && smsSenderService.send(message)) {
      updateMessageAsSent(message);
    }
  }

  @Override
  @RabbitListener(queues = "${amqp.notification.queues.app}")
  public void receiveNotification(MessageDto message) {
    var existedMessage = getMessageByEventId(message);

    if (isMessageSent(existedMessage)) {
      log.error(FOUND_DUPLICATED_NOTIFICATION + message);
      return;
    }

    if (isMessageEmpty(existedMessage)) {
      var createdNotification = Message.builder()
          .eventId(message.getEventId())
          .deviceToken(message.getToken())
          .message(message.getMessage())
          .messageType(MessageType.NOTIFICATION)
          .build();

      messageRepository.save(createdNotification);
    }

    if (isMessageNotSent(existedMessage) && firebaseMessagingService.sendNotification(message)) {
      updateMessageAsSent(message);
    }
  }

  private Message getMessageByEventId(MessageDto message) {
    return messageRepository.findByEventId(message.getEventId())
        .orElse(null);
  }

  private boolean isMessageSent(Message message) {
    return Objects.nonNull(message) && message.isSent();
  }

  private boolean isMessageEmpty(Message message) {
    return Objects.isNull(message);
  }

  private boolean isMessageNotSent(Message message) {
    return isMessageEmpty(message) || !message.isSent();
  }

  private void updateMessageAsSent(MessageDto message) {
    var savedMessage = getMessageByEventId(message);

    if (Objects.nonNull(savedMessage)) {
      savedMessage.setSent(Boolean.TRUE);
      messageRepository.save(savedMessage);
    }
  }
}
