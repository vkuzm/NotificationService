package com.example.project.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class PhoneParser {

  private final static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  public static PhoneNumber parse(String phoneNumber) {
    try {
      return phoneNumberUtil.parse(phoneNumber, "");

    } catch (NumberParseException e) {
      log.error("Error occurred while parsing phone number: " + phoneNumber);
    }

    return new PhoneNumber();
  }
}
