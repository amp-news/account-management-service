package com.amp.accountmanagement.model.dto;

import java.util.Optional;
import javax.validation.constraints.Size;

import com.amp.accountmanagement.model.dto.constraint.Nickname;
import com.amp.accountmanagement.model.entity.AccountStatus;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AccountPartialInputTO {

  private Long accountId;

  @Size(max = 100)
  private String firstName;

  @Size(max = 100)
  private String lastName;

  @Nickname
  private String nickname;

  private AccountStatus status;
  private String role;

  public Long getAccountId() {
    return accountId;
  }

  public Optional<String> getNickname() {
    return Optional.ofNullable(nickname);
  }

  public Optional<String> getFirstName() {
    return Optional.ofNullable(firstName);
  }

  public Optional<String> getLastName() {
    return Optional.ofNullable(lastName);
  }

  public Optional<AccountStatus> getStatus() {
    return Optional.ofNullable(status);
  }

  public Optional<String> getRole() {
    return Optional.ofNullable(role);
  }
}
