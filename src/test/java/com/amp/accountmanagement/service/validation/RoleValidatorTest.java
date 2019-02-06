package com.amp.accountmanagement.service.validation;

import com.amp.accountmanagement.model.entity.Role;
import com.amp.accountmanagement.repository.RoleRepository;
import com.amp.accountmanagement.service.error.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleValidatorTest {

  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private RoleValidator roleValidator;

  @Mock
  private Role role;

  private static final String ROLE_NAME = "role";

  @Test
  public void validate_validationSucceeded() {
    // given
    when(role.getName()).thenReturn(ROLE_NAME);

    // when
    roleValidator.validate(role);

    // then
    verify(roleRepository).existsByName(ROLE_NAME);
  }

  @Test(expected = ValidationException.class)
  public void validate_nameIsUsed_validationFailed() {
    // given
    when(role.getName()).thenReturn(ROLE_NAME);
    when(roleRepository.existsByName(ROLE_NAME)).thenReturn(true);

    // when
    roleValidator.validate(role);

    // then
  }
}
