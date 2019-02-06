package com.amp.accountmanagement.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.amp.accountmanagement.model.dto.RoleTO;
import com.amp.accountmanagement.model.entity.Role;
import com.amp.accountmanagement.repository.AccountRepository;
import com.amp.accountmanagement.repository.RoleRepository;
import com.amp.accountmanagement.service.converter.Converter;
import com.amp.accountmanagement.service.error.ResourceNotFoundException;
import com.amp.accountmanagement.service.error.ServiceException;
import com.amp.accountmanagement.service.error.ValidationException;
import com.amp.accountmanagement.service.impl.RoleServiceImpl;
import com.amp.accountmanagement.service.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
public class RoleServiceImplTest {

  @Mock
  private RoleRepository roleRepository;
  @Mock
  private Converter<Role, RoleTO> roleConverter;
  @Mock
  private Validator<Role> roleValidator;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private RoleServiceImpl roleService;

  @Mock
  private Role role;
  @Mock
  private RoleTO roleTO;

  private static final Long ID = 123L;

  @Before
  public void setUp() {
    when(role.getId()).thenReturn(ID);
    when(roleTO.getRoleId()).thenReturn(ID);

    when(roleConverter.toEntity(roleTO)).thenReturn(role);
    when(roleConverter.toResource(role)).thenReturn(roleTO);
  }

  // RETRIEVE ALL ------------------------------------------------------------------------------------------------------

  @Test
  public void retrieveAll_shouldReturnExpectedItems() {
    // given
    Role role2 = mock(Role.class);
    RoleTO roleTO2 = mock(RoleTO.class);

    when(roleConverter.toResource(role2)).thenReturn(roleTO2);
    when(roleRepository.findAll()).thenReturn(Arrays.asList(role, role2));

    // when
    Collection<RoleTO> result = roleService.retrieveAll();

    // then
    assertThat(result, hasSize(2));
    assertThat(result, hasItems(roleTO, roleTO2));
  }

  // RETRIEVE ----------------------------------------------------------------------------------------------------------

  @Test
  public void retrieve_shouldReturnExpectedItem() {
    // given
    when(roleRepository.findById(ID)).thenReturn(Optional.of(role));

    // when
    RoleTO result = roleService.retrieve(ID);

    // then
    assertThat(result, is(roleTO));
  }

  @Test(expected = ResourceNotFoundException.class)
  public void retrieve_shouldTrowResourceNotFound() {
    // given
    when(roleRepository.findById(ID)).thenReturn(Optional.empty());

    // when
    roleService.retrieve(ID);

    // then
    verify(roleConverter, never()).toResource(role);
  }

  // SAVE --------------------------------------------------------------------------------------------------------------

  @Test
  public void save_shouldSaveItem() {
    // given
    when(roleRepository.save(role)).thenReturn(role);

    // when
    Long id = roleService.save(roleTO);

    // then
    assertThat(id, is(ID));
    verify(roleValidator).validate(role);
  }

  @Test(expected = ValidationException.class)
  public void save_shouldThrowValidationException() {
    // given
    doThrow(ValidationException.class).when(roleValidator).validate(role);

    // when
    roleService.save(roleTO);

    // then
    verify(roleRepository, never()).save(role);
  }

  // UPDATE ------------------------------------------------------------------------------------------------------------

  @Test
  public void update_shouldUpdateItem() {
    // given
    when(roleRepository.existsById(ID)).thenReturn(true);

    // when
    roleService.update(roleTO);

    // then
    verify(roleRepository).save(role);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void update_shouldTrowResourceNotFound() {
    // given
    when(roleRepository.existsById(ID)).thenReturn(false);

    // when
    roleService.update(roleTO);

    // then
    verify(roleConverter, never()).toResource(role);
  }

  @Test(expected = ValidationException.class)
  public void update_shouldThrowValidationException() {
    // given
    when(roleRepository.existsById(ID)).thenReturn(true);
    doThrow(ValidationException.class).when(roleValidator).validate(role);

    // when
    roleService.update(roleTO);

    // then
    verify(roleRepository, never()).save(role);
  }

  // DELETE ------------------------------------------------------------------------------------------------------------

  @Test
  public void delete_shouldDeleteItem() {
    // given
    when(roleRepository.findById(ID)).thenReturn(Optional.of(role));

    // when
    roleService.delete(ID);

    // then
    verify(roleRepository).deleteById(ID);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void delete_shouldTrowResourceNotFound() {
    // given
    when(roleRepository.findById(ID)).thenReturn(Optional.empty());

    // when
    roleService.delete(ID);

    // then
    verify(accountRepository, never()).existsByRole(role);
    verify(roleRepository, never()).deleteById(ID);
  }

  @Test(expected = ServiceException.class)
  public void delete_shouldTrowServiceException() {
    // given
    when(roleRepository.findById(ID)).thenReturn(Optional.of(role));
    when(accountRepository.existsByRole(role)).thenReturn(true);

    // when
    roleService.delete(ID);

    // then
    verify(roleRepository, never()).deleteById(ID);
  }
}
