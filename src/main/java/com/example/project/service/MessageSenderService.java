package com.example.project.service;

import com.example.project.dto.MessageDto;

public interface MessageSenderService {

  void send(MessageDto message);
}
