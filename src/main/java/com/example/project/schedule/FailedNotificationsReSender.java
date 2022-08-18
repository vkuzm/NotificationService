package com.example.project.schedule;

import com.example.project.enums.MessageType;
import com.example.project.model.Message;
import com.example.project.repository.MessageRepository;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class FailedNotificationsReSender {

  private final AmqpTemplate rabbitTemplate;
  private final MessageRepository messageRepository;

  @Value("${amqp.notification.exchange}")
  private String notificationExchange;

  @Value("${amqp.notification.routingkeys.email}")
  private String emailRoutingKey;

  @Value("${amqp.notification.routingkeys.sms}")
  private String smsRoutingKey;

  @Async
  @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
  @Transactional
  public void run() {
    resendFailedEmails();
    resendFailedSms();
  }

  private void resendFailedEmails() {
    List<Message> emailList = messageRepository.findAllByMessageTypeAndSentFalse(MessageType.EMAIL);

    log.info("Email to resend: " + emailList.size());

    for (Message message : emailList) {
      rabbitTemplate.convertAndSend(notificationExchange, emailRoutingKey, message);
    }
  }

  private void resendFailedSms() {
    List<Message> smsList = messageRepository.findAllByMessageTypeAndSentFalse(MessageType.SMS);

    log.info("Sms to resend: " + smsList.size());

    for (Message message : smsList) {
      rabbitTemplate.convertAndSend(notificationExchange, smsRoutingKey, message);
    }
  }
}
