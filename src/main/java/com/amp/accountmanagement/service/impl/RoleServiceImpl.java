package com.amp.accountmanagement.service.impl;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

import com.amp.accountmanagement.model.dto.RoleTO;
import com.amp.accountmanagement.model.entity.Role;
import com.amp.accountmanagement.service.error.ResourceNotFoundException;
import com.amp.accountmanagement.service.error.ServiceException;
import com.amp.accountmanagement.repository.AccountRepository;
import com.amp.accountmanagement.repository.RoleRepository;
import com.amp.accountmanagement.service.RoleService;
import com.amp.accountmanagement.service.converter.Converter;
import com.amp.accountmanagement.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.amp.accountmanagement.model.dto.RoleTO.RESOURCE_NAME;

@Service
public class RoleServiceImpl implements RoleService {

  private AccountRepository accountRepository;
  private RoleRepository roleRepository;
  private Converter<Role, RoleTO> converter;
  private Validator<Role> roleValidator;

  @Autowired
  public RoleServiceImpl(
      AccountRepository accountRepository,
      RoleRepository roleRepository,
      Converter<Role, RoleTO> converter,
      Validator<Role> roleValidator) {
    this.accountRepository = accountRepository;
    this.roleRepository = roleRepository;
    this.converter = converter;
    this.roleValidator = roleValidator;
  }

  @Override
  public Collection<RoleTO> retrieveAll() {
    return roleRepository.findAll().stream().map(converter::toResource).collect(Collectors.toSet());
  }

  @Override
  public RoleTO retrieve(@NotNull Long id) {
    return roleRepository
        .findById(id)
        .map(converter::toResource)
        .orElseThrow(() -> new ResourceNotFoundException(id, RESOURCE_NAME));
  }

  @Override
  @Transactional
  public Long save(@NotNull RoleTO roleTO) {
    final Role role = converter.toEntity(roleTO);
    roleValidator.validate(role);
    return roleRepository.save(role).getId();
  }

  @Override
  @Transactional
  public void update(@NotNull RoleTO roleTO) {
    if (roleRepository.existsById(roleTO.getRoleId())) {
      final Role role = converter.toEntity(roleTO);
      roleValidator.validate(role);
      roleRepository.save(role);
    }
    else {
      throw new ResourceNotFoundException(roleTO.getRoleId(), RESOURCE_NAME);
    }
  }

  @Override
  @Transactional
  public void delete(@NotNull Long id) {
    final Optional<Role> role = roleRepository.findById(id);
    if (!role.isPresent()) {
      throw new ResourceNotFoundException(id, RESOURCE_NAME);
    }
    if (accountRepository.existsByRole(role.get())) {
      throw new ServiceException(
          "Cannot delete role when there is at least one account referencing to it.");
    }

    roleRepository.deleteById(id);
  }
}
