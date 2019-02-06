package com.amp.accountmanagement.service.validation;

import com.amp.accountmanagement.model.dto.RoleTO;
import com.amp.accountmanagement.model.entity.Role;
import com.amp.accountmanagement.service.error.ValidationException;
import com.amp.accountmanagement.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator implements Validator<Role> {

  private RoleRepository roleRepository;

  @Override
  public void validate(Role entity) {
    if (roleRepository.existsByName(entity.getName())) {
      throw new ValidationException(
          RoleTO.RESOURCE_NAME, "There is already role with id specified.");
    }
  }
}
