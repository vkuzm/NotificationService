package com.example.project.dto;

import com.example.project.enums.MessageError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorMessageDto {

  private MessageError errorCode;
  private String errorMessage;
}
