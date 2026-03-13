package com.gamyeon.user.application.service;

import java.util.regex.Pattern;

public class NicknameResolver {

  private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]{1,8}$");
  private static final int MAX_NICKNAME_LENGTH = 8;

  public String resolve(String providerNickname, String email) {
    if (providerNickname != null && NICKNAME_PATTERN.matcher(providerNickname).matches()) {
      return providerNickname;
    }
    return extractFromEmail(email);
  }

  private String extractFromEmail(String email) {
    if (email == null || email.isBlank()) {
      return "user" + (int) (Math.random() * 100000);
    }
    String localPart = email.contains("@") ? email.split("@")[0] : email;
    if (localPart.length() > MAX_NICKNAME_LENGTH) {
      localPart = localPart.substring(0, MAX_NICKNAME_LENGTH);
    }
    return localPart;
  }
}
