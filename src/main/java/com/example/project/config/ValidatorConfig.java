package com.example.project.config;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ValidatorConfig {

  @Bean
  public EmailValidator emailValidator() {
    return EmailValidator.getInstance();
  }

  @Bean
  public PhoneNumberUtil phoneNumberUtil() {
    return PhoneNumberUtil.getInstance();
  }
}
