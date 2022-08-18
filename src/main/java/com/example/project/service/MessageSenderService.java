package com.example.project.service;

import com.example.project.dto.MessageDto;

public interface MessageSenderService {

  void sendEmail(String email, MessageDto message);

  void sendSms(String phoneNumber, MessageDto message);

  void sendNotification(String token, MessageDto message);
}
