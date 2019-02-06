package com.amp.accountmanagement.service.converter;

import com.amp.accountmanagement.controller.endpoint.RoleController;
import com.amp.accountmanagement.model.dto.RoleTO;
import com.amp.accountmanagement.model.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter extends BaseConverter<Role, RoleTO> {

  public RoleConverter() {
    super(RoleController.class, Role.class, RoleTO.class);
  }

  @Override
  protected void addCustomMappings(ModelMapper modelMapper) {
    modelMapper.typeMap(oType, dtoType).addMapping(Role::getId, RoleTO::setRoleId);
    modelMapper.typeMap(dtoType, oType).addMapping(RoleTO::getRoleId, Role::setId);
  }
}
