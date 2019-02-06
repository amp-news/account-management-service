package com.amp.accountmanagement.service.converter;

import com.amp.accountmanagement.controller.endpoint.AccountController;
import com.amp.accountmanagement.controller.endpoint.RoleController;
import com.amp.accountmanagement.model.dto.AccountTO;
import com.amp.accountmanagement.model.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class AccountConverter extends BaseConverter<Account, AccountTO> {

  public AccountConverter() {
    super(AccountController.class, Account.class, AccountTO.class);
  }

  @Override
  protected void addCustomMappings(ModelMapper modelMapper) {
    modelMapper
        .typeMap(oType, dtoType)
        .addMapping(Account::getId, AccountTO::setAccountId)
        .addMapping(source -> source.getRole().getName(), AccountTO::setRole);
    modelMapper.typeMap(dtoType, oType).addMapping(AccountTO::getAccountId, Account::setId);
  }

  @Override
  protected void addCustomLinks(AccountTO resource) {
    resource.add(linkTo(RoleController.class).slash(resource.getRoleId()).withRel("role"));
  }
}
