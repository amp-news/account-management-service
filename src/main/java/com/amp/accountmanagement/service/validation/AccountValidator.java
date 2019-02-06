package com.amp.accountmanagement.service.validation;

import java.util.Optional;

import com.amp.accountmanagement.model.dto.AccountTO;
import com.amp.accountmanagement.model.entity.Account;
import com.amp.accountmanagement.repository.AccountRepository;
import com.amp.accountmanagement.service.error.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator implements Validator<Account> {

  private AccountRepository accountRepository;

  @Autowired
  public AccountValidator(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public void validate(Account entity) {
    Optional<Account> account = accountRepository.findAccountByEmail(entity.getEmail());
    if (account.isPresent() && !account.get().getId().equals(entity.getId())) {
      throw new ValidationException(
          AccountTO.RESOURCE_NAME, "Account with specified email already exists.");
    }

    if (entity.getRole() == null) {
      throw new ValidationException(
          AccountTO.RESOURCE_NAME, "There is no role with roleId specified.");
    }
  }
}
