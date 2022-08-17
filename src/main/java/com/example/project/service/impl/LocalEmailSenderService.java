package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.enums.MessageType;
import com.example.project.service.EmailSenderService;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class LocalEmailSenderService implements EmailSenderService {

  private final JavaMailSender emailSender;

  @Override
  public boolean send(MessageDto message) {
    try {
      var mailMessage = emailSender.createMimeMessage();
      var helper = new MimeMessageHelper(mailMessage, "UTF-8");
      helper.setFrom(new InternetAddress(message.getSender()));
      helper.setTo(message.getReceiver());
      helper.setSubject(message.getSubject());
      helper.setText(message.getMessage(),message.getMessageType() == MessageType.HTML);

      emailSender.send(mailMessage);
      log.info("Email was sent: " + message);
      return true;

    } catch (MailException | MessagingException e) {
      log.error("Email was not sent: " + message, e);
      return false;
    }
  }
}
