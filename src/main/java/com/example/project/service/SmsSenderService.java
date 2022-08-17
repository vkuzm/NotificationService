package com.example.project.service;

import com.example.project.dto.MessageDto;

public interface SmsSenderService {

  boolean send(MessageDto message);
}
