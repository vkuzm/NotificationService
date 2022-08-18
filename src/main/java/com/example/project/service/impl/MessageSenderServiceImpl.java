package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.service.MessageSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("messageEmailSenderService")
@RequiredArgsConstructor
public class MessageSenderServiceImpl implements MessageSenderService {

  private final AmqpTemplate rabbitTemplate;

  @Value("${amqp.notification.exchange}")
  private String notificationExchange;

  @Value("${amqp.notification.routingkeys.email}")
  private String emailRoutingKey;

  @Value("${amqp.notification.routingkeys.sms}")
  private String smsRoutingKey;

  @Value("${amqp.notification.routingkeys.app}")
  private String appRoutingKey;

  @Override
  public void sendEmail(String receiverEmail, MessageDto message) {
    message.setReceiver(receiverEmail);

    rabbitTemplate.convertAndSend(notificationExchange, emailRoutingKey, message);
  }

  @Override
  public void sendSms(String receiverPhoneNumber, MessageDto message) {
    message.setReceiver(receiverPhoneNumber);

    rabbitTemplate.convertAndSend(notificationExchange, smsRoutingKey, message);
  }

  @Override
  public void sendNotification(String token, MessageDto message) {
    message.setToken(token);

    rabbitTemplate.convertAndSend(notificationExchange, appRoutingKey, message);
  }
}