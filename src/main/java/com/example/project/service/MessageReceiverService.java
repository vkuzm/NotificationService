package com.example.project.service;

import com.example.project.dto.MessageDto;

public interface MessageReceiverService {

  void receiveEmail(MessageDto message);

  void receiveSms(MessageDto message);
}
