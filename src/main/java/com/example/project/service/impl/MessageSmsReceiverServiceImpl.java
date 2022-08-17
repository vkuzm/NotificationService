package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.model.Message;
import com.example.project.repository.SmsRepository;
import com.example.project.service.MessageReceiverService;
import com.example.project.service.SmsSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service("messageSmsReceiverService")
@RequiredArgsConstructor
@Log4j2
public class MessageSmsReceiverServiceImpl implements MessageReceiverService {

  private static final String FOUND_DUPLICATED_SMS = "Found duplicated sms: ";

  private final SmsSenderService smsSenderService;
  private final SmsRepository smsRepository;

  @Override
  @RabbitListener(queues = "${amqp.notification.queues.sms}")
  public void receive(MessageDto message) {
    var existedSms = smsRepository.findByEventId(message.getEventId());

    if (existedSms.isPresent() && existedSms.get().isSent()) {
      log.error(FOUND_DUPLICATED_SMS + message);
      return;
    }

    if (existedSms.isEmpty()) {
      insertSmsMessage(message);
    }

    if (existedSms.isEmpty() || !existedSms.get().isSent()) {
      if (smsSenderService.send(message)) {
        updateSmsAsSent(message);
      }
    }
  }

  private void insertSmsMessage(MessageDto message) {
    var createdEmail = Message.builder()
        .eventId(message.getEventId())
        .receiver(message.getReceiver())
        .sender(message.getSender())
        .subject(message.getSubject())
        .message(message.getMessage())
        .build();

    smsRepository.save(createdEmail);
  }

  private void updateSmsAsSent(MessageDto message) {
    var savedEmail = smsRepository.findByEventId(message.getEventId());
    savedEmail.ifPresent(email -> {
      email.setSent(Boolean.TRUE);
      smsRepository.save(email);
    });
  }
}
