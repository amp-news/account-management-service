package com.amp.accountmanagement.service.impl;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amp.accountmanagement.model.dto.AccountStatusTO;
import com.amp.accountmanagement.model.entity.AccountStatus;
import com.amp.accountmanagement.service.AccountStatusService;
import org.springframework.stereotype.Service;

@Service
public class AccountStatusServiceImpl implements AccountStatusService {

  @Override
  public Set<AccountStatusTO> retrieveAll() {
    return Stream.of(AccountStatus.values()).map(this::toResource).collect(Collectors.toSet());
  }

  private AccountStatusTO toResource(AccountStatus accountStatus) {
    return new AccountStatusTO(accountStatus.getId(), accountStatus.getDisplayName());
  }
}
