package com.amp.accountmanagement.service;

import java.util.Collection;
import javax.validation.constraints.NotNull;

import com.amp.accountmanagement.model.dto.RoleTO;

public interface RoleService {

  Collection<RoleTO> retrieveAll();

  RoleTO retrieve(@NotNull Long id);

  Long save(@NotNull RoleTO role);

  void update(@NotNull RoleTO role);

  void delete(@NotNull Long id);
}
