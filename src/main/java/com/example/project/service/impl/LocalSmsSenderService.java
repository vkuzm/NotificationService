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
    log.info("Sms was sent: " + message);
    return true;
  }
}
