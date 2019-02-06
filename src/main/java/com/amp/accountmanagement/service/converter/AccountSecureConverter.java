package com.amp.accountmanagement.service.converter;

import com.amp.accountmanagement.controller.endpoint.AccountController;
import com.amp.accountmanagement.controller.endpoint.RoleController;
import com.amp.accountmanagement.model.dto.AccountTOSecured;
import com.amp.accountmanagement.model.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class AccountSecureConverter extends BaseConverter<Account, AccountTOSecured> {

  public AccountSecureConverter() {
    super(AccountController.class, Account.class, AccountTOSecured.class);
  }

  @Override
  protected void addCustomMappings(ModelMapper modelMapper) {
    modelMapper
        .typeMap(oType, dtoType)
        .addMapping(Account::getId, AccountTOSecured::setAccountId)
        .addMapping(source -> source.getRole().getName(), AccountTOSecured::setRole);
    modelMapper.typeMap(dtoType, oType).addMapping(AccountTOSecured::getAccountId, Account::setId);
  }

  @Override
  protected void addCustomLinks(AccountTOSecured resource) {
    resource.add(linkTo(RoleController.class).slash(resource.getRoleId()).withRel("role"));
  }
}
