package com.example.project.service;

import com.example.project.dto.MessageDto;

public interface EmailSenderService {

  boolean send(MessageDto message);
}
