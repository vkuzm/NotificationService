package com.example.project.service;

import com.example.project.dto.MessageDto;

public interface FirebaseMessagingService {

  boolean sendNotification(MessageDto message);
}
