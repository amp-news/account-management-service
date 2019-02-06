package com.amp.accountmanagement.service.validation;

import java.util.Optional;

import com.amp.accountmanagement.model.entity.Account;
import com.amp.accountmanagement.model.entity.Role;
import com.amp.accountmanagement.repository.AccountRepository;
import com.amp.accountmanagement.service.error.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountValidatorTest {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountValidator accountValidator;

  @Mock
  private Account account;

  @Mock
  private Account anotherAccount;

  @Mock
  private Role role;

  public static final String EMAIL = "email";

  @Before
  public void setUp() {
    when(account.getEmail()).thenReturn(EMAIL);
    when(account.getRole()).thenReturn(role);
    when(account.getId()).thenReturn(123L);
    when(anotherAccount.getId()).thenReturn(321L);

    when(accountRepository.findAccountByEmail(EMAIL)).thenReturn(Optional.empty());
  }

  @Test
  public void validate_validationSucceeded() {
    // given

    // when
    accountValidator.validate(account);

    // then
    verify(accountRepository).findAccountByEmail(EMAIL);
  }

  @Test(expected = ValidationException.class)
  public void validate_usernameIsUsed_validationFailed() {
    // given
    when(accountRepository.findAccountByEmail(EMAIL)).thenReturn(Optional.of(anotherAccount));

    // when
    accountValidator.validate(account);

    // then
    verify(accountRepository, never()).findAccountByEmail(EMAIL);
  }

  @Test(expected = ValidationException.class)
  public void validate_emailIsUsed_validationFailed() {
    // given
    when(accountRepository.findAccountByEmail(EMAIL)).thenReturn(Optional.of(anotherAccount));

    // when
    accountValidator.validate(account);

    // then
    verify(accountRepository).findAccountByEmail(EMAIL);
  }

  @Test(expected = ValidationException.class)
  public void validate_noRole_validationFailed() {
    // given
    when(account.getRole()).thenReturn(null);

    // when
    accountValidator.validate(account);

    // then
    verify(accountRepository).findAccountByEmail(EMAIL);
  }
}
