package com.amp.accountmanagement.model.entity;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.hateoas.Identifiable;

public enum AccountStatus implements Identifiable<String> {
  ACTIVE("Active"),
  INACTIVE("Inactive");

  private String name;

  AccountStatus(String name) {
    this.name = name;
  }

  @JsonValue
  public String getDisplayName() {
    return this.name;
  }

  @Override
  public String getId() {
    return this.name();
  }

  @JsonCreator
  public static AccountStatus fromName(String name) {
    return Stream.of(AccountStatus.values())
        .filter(accountStatus -> accountStatus.getDisplayName().equals(name))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
