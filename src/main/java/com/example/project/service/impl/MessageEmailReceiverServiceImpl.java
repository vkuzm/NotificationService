package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.model.Message;
import com.example.project.repository.EmailRepository;
import com.example.project.service.EmailSenderService;
import com.example.project.service.MessageReceiverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service("messageEmailReceiverService")
@RequiredArgsConstructor
@Log4j2
public class MessageEmailReceiverServiceImpl implements MessageReceiverService {

  private static final String FOUND_DUPLICATED_EMAIL = "Found duplicated email: ";

  private final EmailSenderService emailSenderService;
  private final EmailRepository emailRepository;

  @Override
  @RabbitListener(queues = "${amqp.notification.queues.email}")
  public void receive(MessageDto message) {
    var existedEmail = emailRepository.findByEventId(message.getEventId());

    if (existedEmail.isPresent() && existedEmail.get().isSent()) {
      log.error(FOUND_DUPLICATED_EMAIL + message);
      return;
    }

    if (existedEmail.isEmpty()) {
      insertEmailMessage(message);
    }

    if (existedEmail.isEmpty() || !existedEmail.get().isSent()) {
      if (emailSenderService.send(message)) {
        updateEmailAsSent(message);
      }
    }
  }

  private void insertEmailMessage(MessageDto message) {
    var createdEmail = Message.builder()
        .eventId(message.getEventId())
        .receiver(message.getReceiver())
        .sender(message.getSender())
        .subject(message.getSubject())
        .message(message.getMessage())
        .messageType(message.getMessageType())
        .build();

    emailRepository.save(createdEmail);
  }

  private void updateEmailAsSent(MessageDto message) {
    var savedEmail = emailRepository.findByEventId(message.getEventId());
    savedEmail.ifPresent(email -> {
      email.setSent(Boolean.TRUE);
      emailRepository.save(email);
    });
  }
}
