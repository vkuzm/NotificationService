package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.service.MessageSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("messageEmailSenderService")
@RequiredArgsConstructor
public class MessageEmailSenderServiceImpl implements MessageSenderService {

  private final AmqpTemplate rabbitTemplate;

  @Value("${amqp.notification.exchange}")
  private String notificationExchange;

  @Value("${amqp.notification.routingkeys.email}")
  private String emailRoutingKey;

  @Override
  public void send(MessageDto message) {
    rabbitTemplate.convertAndSend(notificationExchange, emailRoutingKey, message);
  }
}
