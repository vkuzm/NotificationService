package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.service.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

  private static final String NOTIFICATION_SENT_MESSAGE = "Notification was sent: ";
  private static final String NOTIFICATION_NOT_SENT_MESSAGE = "Notification was not sent: ";

  private final FirebaseMessaging firebaseMessaging;

  @Override
  public boolean sendNotification(MessageDto message) {
    var notification = Notification
        .builder()
        .setTitle(message.getSubject())
        .setBody(message.getMessage())
        .build();

    var createdMessage = Message
        .builder()
        .setToken(message.getToken())
        .setNotification(notification)
        .build();

    try {
      firebaseMessaging.send(createdMessage);
      log.info(NOTIFICATION_SENT_MESSAGE + message);
      return true;

    } catch (FirebaseMessagingException e) {
      log.error(NOTIFICATION_NOT_SENT_MESSAGE + message, e);
      return false;
    }
  }
}
