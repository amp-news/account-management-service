package com.amp.accountmanagement.model.entity;

import java.util.stream.Stream;

public enum Claim {

  TOKEN_TYPE("tokenType"),
  USER_EMAIL("email"),
  USER_STATUS("status"),
  USER_ROLE("role");

  private String name;

  Claim(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public static Claim fromName(String name) {
    return Stream.of(Claim.values())
        .filter(claim -> claim.name.equals(name))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
