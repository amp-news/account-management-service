package com.amp.accountmanagement.service.converter;

import com.amp.accountmanagement.model.dto.AccountTO;
import com.amp.accountmanagement.model.entity.Account;
import com.amp.accountmanagement.model.entity.AccountStatus;
import com.amp.accountmanagement.model.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(MockitoJUnitRunner.class)
public class AccountConverterTest {

  private AccountConverter converter = new AccountConverter();

  @Test
  public void toEntity() {
    // given
    AccountTO accountTO = new AccountTO();
    accountTO.setAccountId(123L);
    accountTO.setNickname("user");
    accountTO.setEmail("email");
    accountTO.setStatus(AccountStatus.ACTIVE);
    accountTO.setRoleId(321L);

    // when
    Account account = converter.toEntity(accountTO);

    // then
    assertThat(account.getId(), is(accountTO.getAccountId()));
    assertThat(account.getNickname(), is(accountTO.getNickname()));
    assertThat(account.getEmail(), is(accountTO.getEmail()));
    assertThat(account.getStatus(), is(accountTO.getStatus()));
    assertThat(account.getFirstName(), is(nullValue()));
    assertThat(account.getLastName(), is(nullValue()));
    assertThat(account.getPassword(), is(nullValue()));
    assertThat(account.getRole(), is(nullValue()));
  }

  @Test
  public void toResource() {
    // given
    Role role = new Role();
    role.setId(123L);
    role.setName("role");

    Account account = new Account();
    account.setId(123L);
    account.setNickname("user");
    account.setFirstName("fName");
    account.setLastName("lName");
    account.setEmail("email");
    account.setStatus(AccountStatus.ACTIVE);
    account.setRole(role);

    // when
    AccountTO accountTO = converter.toResource(account);

    // then
    assertThat(accountTO.getAccountId(), is(account.getId()));
    assertThat(accountTO.getNickname(), is(account.getNickname()));
    assertThat(accountTO.getEmail(), is(account.getEmail()));
    assertThat(accountTO.getStatus(), is(account.getStatus()));
    assertThat(accountTO.getFirstName(), is(account.getFirstName()));
    assertThat(accountTO.getLastName(), is(account.getLastName()));
    assertThat(accountTO.getRole(), is(account.getRole().getName()));
  }
}
