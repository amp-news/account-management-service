package com.amp.accountmanagement.model.dto;

import lombok.Data;

@Data
public class AccountStatusTO {

  public static final String RESOURCE_NAME = "Status";

  private final String id;
  private final String name;
}
