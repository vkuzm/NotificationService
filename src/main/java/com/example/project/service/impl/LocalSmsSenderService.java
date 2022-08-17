package com.example.project.service.impl;

import com.example.project.dto.MessageDto;
import com.example.project.service.SmsSenderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LocalSmsSenderService implements SmsSenderService {

  @Override
  public boolean send(MessageDto message) {
    int res = (int) Math.floor(Math.random() * 2);

    if (res == 1) {
      log.info("Sms was sent: " + message);
      return true;

    } else {
      log.info("Sms was not sent: " + message);
      return false;
    }
  }
}
