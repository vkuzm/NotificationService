package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.enums.MessageFormat;
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

  private static final String MESSAGE_ENCODING = "UTF-8";
  private static final String EMAIL_SENT_MESSAGE = "Email was sent: ";
  private static final String EMAIL_NOT_SENT_MESSAGE = "Email was not sent: ";

  private final JavaMailSender emailSender;

  @Override
  public boolean send(MessageDto message) {
    try {
      var mailMessage = emailSender.createMimeMessage();
      var helper = new MimeMessageHelper(mailMessage, MESSAGE_ENCODING);
      helper.setFrom(new InternetAddress(message.getSender()));
      helper.setTo(message.getReceiver());
      helper.setSubject(message.getSubject());
      helper.setText(message.getMessage(), message.getMessageFormat().equals(MessageFormat.HTML.name()));

      emailSender.send(mailMessage);
      log.info(EMAIL_SENT_MESSAGE + message);
      return true;

    } catch (MailException | MessagingException e) {
      log.error(EMAIL_NOT_SENT_MESSAGE + message, e);
      return false;
    }
  }
}
