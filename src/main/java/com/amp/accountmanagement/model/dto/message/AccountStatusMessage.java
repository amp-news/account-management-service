package com.amp.accountmanagement.model.dto.message;

import java.time.Instant;

import com.amp.accountmanagement.model.entity.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountStatusMessage implements Message{

  private static final String ACCOUNT_PATH = "account";
  private static final String BLOCKED_PATH = ACCOUNT_PATH + ".user.blocked";
  private static final String UNBLOCKED_PATH = ACCOUNT_PATH + ".user.unblocked";

  private Long accountId;
  private String firstName;
  private String lastName;
  private AccountStatus status;
  private Instant timestamp;

  @JsonIgnore
  @Override
  public String getPath() {
    String path = "";
    switch (status) {
      case ACTIVE:
        path = UNBLOCKED_PATH;
        break;
      case INACTIVE:
        path = BLOCKED_PATH;
        break;
    }

    return path;
  }
}
