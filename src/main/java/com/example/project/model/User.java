package com.example.project.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class User {

  @Id
  private String id;
  private String email;
  private String phoneNumber;
  private List<NotificationSetting> notificationSettings;
}
