package com.example.project.population;

import com.example.project.enums.MessageType;
import com.example.project.model.NotificationSetting;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopulateUsers implements ApplicationRunner {

  private final UserRepository userRepository;

  @Override
  public void run(ApplicationArguments args) {
    userRepository.deleteAll();

    List<User> users = new ArrayList<>();

    users.add(User.builder()
        .id("1")
        .email("email1@test.com")
        .phoneNumber("+15129985551")
        .notificationSettings(List.of(
            new NotificationSetting(MessageType.EMAIL, true),
            new NotificationSetting(MessageType.SMS, true)
        ))
        .build()
    );

    users.add(User.builder()
        .id("2")
        .email("email2@test.com")
        .phoneNumber("+15129985552")
        .notificationSettings(List.of(
            new NotificationSetting(MessageType.EMAIL, true),
            new NotificationSetting(MessageType.SMS, false)
        ))
        .build()
    );

    users.add(User.builder()
        .id("3")
        .email("email3@test.com")
        .phoneNumber("+15129985553")
        .notificationSettings(List.of(
            new NotificationSetting(MessageType.EMAIL, false),
            new NotificationSetting(MessageType.SMS, true)
        ))
        .build()
    );

    userRepository.saveAll(users);
  }
}
