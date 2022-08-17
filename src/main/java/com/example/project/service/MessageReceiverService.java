package com.example.project.service;

import com.example.project.dto.MessageDto;

public interface MessageReceiverService {

  void receive(MessageDto message);
}
