package com.amp.accountmanagement.service.impl;

import java.util.Arrays;
import java.util.Optional;

import com.amp.accountmanagement.model.dto.AccountPartialInputTO;
import com.amp.accountmanagement.model.dto.AccountTO;
import com.amp.accountmanagement.model.dto.AccountTOSecured;
import com.amp.accountmanagement.model.dto.PageTO;
import com.amp.accountmanagement.model.entity.Account;
import com.amp.accountmanagement.model.entity.Role;
import com.amp.accountmanagement.repository.AccountRepository;
import com.amp.accountmanagement.repository.RoleRepository;
import com.amp.accountmanagement.service.converter.Converter;
import com.amp.accountmanagement.service.error.ResourceNotFoundException;
import com.amp.accountmanagement.service.error.ValidationException;
import com.amp.accountmanagement.service.validation.Validator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

  @Mock private AccountRepository accountRepository;
  @Mock private RoleRepository roleRepository;
  @Mock private Converter<Account, AccountTO> accountConverter;
  @Mock private Converter<Account, AccountTOSecured> accountSecureConverter;
  @Mock private Validator<Account> accountValidator;

  @InjectMocks private AccountServiceImpl accountService;

  @Mock private Account account;
  @Mock private AccountTO accountTO;
  @Mock private AccountTOSecured accountTOSecured;
  @Mock private AccountPartialInputTO accountPartialInputTO;
  @Mock private Page<Account> pageAccount;
  @Mock private Page<AccountTO> pageAccountTO;
  @Mock private PageTO<AccountTO> pageTO;
  @Mock private Role role;

  private static final Long ID = 123L;
  private static final String ROLE_NAME = "role";
  private static final String USERNAME = "user";

  @Before
  public void setUp() {
    when(account.getId()).thenReturn(ID);
    when(accountTO.getAccountId()).thenReturn(ID);
    when(accountTO.getRole()).thenReturn(ROLE_NAME);
    when(accountTOSecured.getAccountId()).thenReturn(ID);
    when(accountTOSecured.getRole()).thenReturn(ROLE_NAME);
    when(accountPartialInputTO.getAccountId()).thenReturn(ID);

    when(accountConverter.toEntity(accountTO)).thenReturn(account);
    when(accountConverter.toResource(account)).thenReturn(accountTO);

    when(accountSecureConverter.toEntity(accountTOSecured)).thenReturn(account);
    when(accountSecureConverter.toResource(account)).thenReturn(accountTOSecured);
  }

  // RETRIEVE ALL
  // ------------------------------------------------------------------------------------------------------

  @Test
  @Ignore
  public void retrieveAll_shouldReturnExpectedItems() {
    // given
    Account account2 = mock(Account.class);
    AccountTO accountTO2 = mock(AccountTO.class);
    Pageable pageable = Pageable.unpaged();

    when(accountRepository.findAll(pageable)).thenReturn(pageAccount);
    when(accountConverter.toResource(account2)).thenReturn(accountTO2);
    when(pageAccountTO.getContent()).thenReturn(Arrays.asList(accountTO, accountTO2));
    when(pageTO.getContent()).thenReturn(Arrays.asList(accountTO, accountTO2));

    // when
    PageTO<AccountTO> result = accountService.retrieveAll(pageable);

    // then
    assertThat(result.getContent(), hasSize(2));
    assertThat(result.getContent(), hasItems(accountTO, accountTO2));
  }

  // RETRIEVE BY ID
  // ----------------------------------------------------------------------------------------------------------

  @Test
  public void retrieveById_shouldReturnExpectedItem() {
    // given
    when(accountRepository.findById(ID)).thenReturn(Optional.of(account));

    // when
    AccountTO result = accountService.retrieve(ID);

    // then
    assertThat(result, is(accountTO));
  }

  @Test(expected = ResourceNotFoundException.class)
  public void retrieveById_shouldTrowResourceNotFound() {
    // given
    when(accountRepository.findById(ID)).thenReturn(Optional.empty());

    // when
    accountService.retrieve(ID);

    // then
    verify(accountConverter, never()).toResource(account);
  }

  // SAVE
  // --------------------------------------------------------------------------------------------------------------

  @Test
  public void save_shouldSaveItem() {
    // given
    when(accountRepository.save(account)).thenReturn(account);
    when(roleRepository.findRoleByName(ROLE_NAME)).thenReturn(Optional.of(role));

    // when
    Long id = accountService.save(accountTOSecured);

    // then
    assertThat(id, is(ID));
    verify(roleRepository).findRoleByName(ROLE_NAME);
    verify(account).setRole(role);
    verify(accountValidator).validate(account);
  }

  @Test(expected = ValidationException.class)
  public void save_noRole_shouldThrowValidationException() {
    // given
    when(roleRepository.findRoleByName(ROLE_NAME)).thenReturn(Optional.empty());
    doThrow(ValidationException.class).when(accountValidator).validate(account);

    // when
    accountService.save(accountTOSecured);

    // then
    verify(account, never()).setRole(role);
    verify(accountRepository, never()).save(account);
  }

  // UPDATE
  // ------------------------------------------------------------------------------------------------------------

  @Test
  public void update_shouldUpdateItem() {
    // given
    when(accountRepository.existsById(ID)).thenReturn(true);
    when(roleRepository.findRoleByName(ROLE_NAME)).thenReturn(Optional.of(role));

    // when
    accountService.update(accountTO);

    // then
    verify(roleRepository).findRoleByName(ROLE_NAME);
    verify(account).setRole(role);
    verify(accountRepository).save(account);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void update_shouldTrowResourceNotFound() {
    // given
    when(accountRepository.existsById(ID)).thenReturn(false);

    // when
    accountService.update(accountTO);

    // then
    verify(accountConverter, never()).toResource(account);
  }

  @Test(expected = ValidationException.class)
  public void update_noRole_shouldThrowValidationException() {
    // given
    when(accountRepository.existsById(ID)).thenReturn(true);
    when(roleRepository.findRoleByName(ROLE_NAME)).thenReturn(Optional.empty());
    doThrow(ValidationException.class).when(accountValidator).validate(account);

    // when
    accountService.update(accountTO);

    // then
    verify(account, never()).setRole(role);
    verify(accountRepository, never()).save(account);
  }

  // UPDATE PARTIAL
  // ----------------------------------------------------------------------------------------------------

  @Test
  public void update_partial_shouldUpdateItem() {
    // given
    when(accountRepository.existsById(ID)).thenReturn(true);
    when(accountRepository.getOne(ID)).thenReturn(account);

    // when
    accountService.update(accountPartialInputTO);

    // then
    verify(accountRepository).save(account);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void update_partial_shouldTrowResourceNotFound() {
    // given
    when(accountRepository.existsById(ID)).thenReturn(false);

    // when
    accountService.update(accountPartialInputTO);

    // then
    verify(accountRepository, never()).getOne(ID);
  }

  @Test(expected = ValidationException.class)
  public void update_partial_shouldThrowValidationException() {
    // given
    when(accountRepository.existsById(ID)).thenReturn(true);
    when(accountRepository.getOne(ID)).thenReturn(account);
    doThrow(ValidationException.class).when(accountValidator).validate(account);

    // when
    accountService.update(accountPartialInputTO);

    // then
    verify(accountRepository, never()).save(account);
  }

  // DELETE
  // ------------------------------------------------------------------------------------------------------------

  @Test
  public void delete_shouldDeleteItem() {
    // given
    when(accountRepository.existsById(ID)).thenReturn(true);

    // when
    accountService.delete(ID);

    // then
    verify(accountRepository).deleteById(ID);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void delete_shouldTrowResourceNotFound() {
    // given
    when(accountRepository.existsById(ID)).thenReturn(false);

    // when
    accountService.delete(ID);

    // then
    verify(accountRepository, never()).deleteById(ID);
  }
}
