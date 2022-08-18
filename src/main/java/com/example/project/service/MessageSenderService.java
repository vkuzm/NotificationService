package com.example.project.service;

import com.example.project.dto.MessageDto;

public interface MessageSenderService {

  void sendEmail(String receiver, MessageDto message);

  void sendSms(String receiver, MessageDto message);
}
