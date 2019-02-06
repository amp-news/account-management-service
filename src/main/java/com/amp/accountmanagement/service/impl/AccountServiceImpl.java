package com.amp.accountmanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

import com.amp.accountmanagement.model.dto.AccountPartialInputTO;
import com.amp.accountmanagement.model.dto.AccountTO;
import com.amp.accountmanagement.model.dto.AccountTOSecured;
import com.amp.accountmanagement.model.dto.PageTO;
import com.amp.accountmanagement.model.dto.message.AccountStatusMessage;
import com.amp.accountmanagement.model.entity.Account;
import com.amp.accountmanagement.model.entity.AccountStatus;
import com.amp.accountmanagement.repository.AccountRepository;
import com.amp.accountmanagement.repository.RoleRepository;
import com.amp.accountmanagement.service.AccountService;
import com.amp.accountmanagement.service.converter.Converter;
import com.amp.accountmanagement.service.error.ResourceNotFoundException;
import com.amp.accountmanagement.service.external.MessageSender;
import com.amp.accountmanagement.service.filter.FilterVisitor;
import com.amp.accountmanagement.service.validation.Validator;
import cz.jirutka.rsql.parser.RSQLParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.amp.accountmanagement.model.dto.AccountTO.RESOURCE_NAME;
import static java.time.Instant.now;

@Service
public class AccountServiceImpl implements AccountService {

  private MessageSender messageSender;
  private RoleRepository roleRepository;
  private AccountRepository accountRepository;
  private Converter<Account, AccountTO> accountConverter;
  private Converter<Account, AccountTOSecured> accountSecureConverter;
  private Validator<Account> accountValidator;
  private RSQLParser filterQueryParser;

  @Autowired
  public AccountServiceImpl(
      MessageSender messageSender,
      RoleRepository roleRepository,
      AccountRepository accountRepository,
      Converter<Account, AccountTO> accountConverter,
      Converter<Account, AccountTOSecured> accountSecureConverter,
      Validator<Account> accountValidator) {
    this.messageSender = messageSender;
    this.roleRepository = roleRepository;
    this.accountRepository = accountRepository;
    this.accountValidator = accountValidator;
    this.accountConverter = accountConverter;
    this.accountSecureConverter = accountSecureConverter;
    this.filterQueryParser = new RSQLParser();
  }

  @Override
  public PageTO<AccountTO> retrieveAll(@NotNull Pageable pageable) {
    return new PageTO<>(accountRepository.findAll(pageable).map(accountConverter::toResource));
  }

  @Override
  public PageTO<AccountTO> retrieveAll(@NotNull String filter, @NotNull Pageable pageable) {
    final Specification<Account> accountSpecification =
        filterQueryParser.parse(filter).accept(new FilterVisitor<>());

    return new PageTO<>(
        accountRepository
            .findAll(accountSpecification, pageable)
            .map(accountConverter::toResource));
  }

  @Override
  public List<AccountTOSecured> retrieveAllSecured(@NotNull String filter) {
    final Specification<Account> accountSpecification =
        filterQueryParser.parse(filter).accept(new FilterVisitor<>());

    return accountRepository
        .findAll(accountSpecification)
        .stream()
        .map(accountSecureConverter::toResource)
        .collect(Collectors.toList());
  }

  @Override
  public AccountTO retrieve(@NotNull Long id) {
    return accountRepository
        .findById(id)
        .map(accountConverter::toResource)
        .orElseThrow(() -> new ResourceNotFoundException(id, RESOURCE_NAME));
  }

  @Override
  @Transactional
  public Long save(@NotNull AccountTOSecured accountTO) {
    final Account account = accountSecureConverter.toEntity(accountTO);
    roleRepository.findRoleByName(accountTO.getRole()).ifPresent(account::setRole);

    accountValidator.validate(account);
    return accountRepository.save(account).getId();
  }

  @Override
  @Transactional
  public void update(@NotNull AccountTO accountTO) {
    if (!accountRepository.existsById(accountTO.getAccountId())) {
      throw new ResourceNotFoundException(accountTO.getAccountId(), RESOURCE_NAME);
    }

    final Account account = accountConverter.toEntity(accountTO);
    roleRepository.findRoleByName(accountTO.getRole()).ifPresent(account::setRole);
    accountValidator.validate(account);
    accountRepository.save(account);
  }

  @Override
  @Transactional
  public void update(@NotNull AccountPartialInputTO accountPartialInputTO) {
    final Long accountId = accountPartialInputTO.getAccountId();
    if (!accountRepository.existsById(accountId)) {
      throw new ResourceNotFoundException(accountPartialInputTO.getAccountId(), RESOURCE_NAME);
    }

    final Account account = accountRepository.getOne(accountId);
    accountPartialInputTO
        .getStatus()
        .ifPresent(accountStatus -> processStatusEvents(account, accountStatus));
    accountPartialInputTO.getNickname().ifPresent(account::setNickname);
    accountPartialInputTO.getFirstName().ifPresent(account::setFirstName);
    accountPartialInputTO.getLastName().ifPresent(account::setLastName);
    accountPartialInputTO.getStatus().ifPresent(account::setStatus);
    accountPartialInputTO
        .getRole()
        .ifPresent(role -> roleRepository.findRoleByName(role).ifPresent(account::setRole));

    accountValidator.validate(account);
    accountRepository.save(account);
  }

  @Override
  @Transactional
  public void delete(@NotNull Long id) {
    if (!accountRepository.existsById(id)) {
      throw new ResourceNotFoundException(id, RESOURCE_NAME);
    }

    accountRepository.deleteById(id);
  }

  private void processStatusEvents(Account account, AccountStatus status) {
    if (account.getStatus() != status) {
      messageSender.sendMessage(
          new AccountStatusMessage(
              account.getId(), account.getFirstName(), account.getLastName(), status, now()));
    }
  }
}
