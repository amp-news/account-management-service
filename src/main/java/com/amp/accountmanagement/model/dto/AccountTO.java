package com.amp.accountmanagement.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.amp.accountmanagement.model.dto.constraint.Nickname;
import com.amp.accountmanagement.model.entity.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
@JsonPropertyOrder({"id", "nickname", "firstName", "lastName"})
public class AccountTO extends ResourceSupport {

  public static final String RESOURCE_NAME = "Account";

  @JsonProperty("id")
  private Long accountId;

  @Nickname
  private String nickname;

  @NotEmpty
  @Email
  private String email;

  @NotNull
  @Size(max = 100)
  private String firstName;

  @NotNull
  @Size(max = 100)
  private String lastName;

  @NotNull
  private AccountStatus status;

  @NotNull
  private String role;

  @JsonIgnore
  private Long roleId;
}
