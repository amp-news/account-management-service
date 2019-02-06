package com.amp.accountmanagement.service.converter;

import com.amp.accountmanagement.model.dto.RoleTO;
import com.amp.accountmanagement.model.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class RoleConverterTest {

  private RoleConverter roleConverter = new RoleConverter();

  @Test
  public void toEntity() {
    // given
    RoleTO roleTO = new RoleTO();
    roleTO.setRoleId(123L);
    roleTO.setName("roleName");
    roleTO.setDescription("description");

    // when
    Role role = roleConverter.toEntity(roleTO);

    // then
    assertThat(role.getId(), is(roleTO.getRoleId()));
    assertThat(role.getName(), is(roleTO.getName()));
    assertThat(role.getDescription(), is(roleTO.getDescription()));
  }

  @Test
  public void toResource() {
    // given
    Role role = new Role();
    role.setId(123L);
    role.setName("roleName");
    role.setDescription("description");

    // when
    RoleTO roleTO = roleConverter.toResource(role);

    // then
    assertThat(roleTO.getRoleId(), is(role.getId()));
    assertThat(roleTO.getName(), is(role.getName()));
    assertThat(roleTO.getDescription(), is(role.getDescription()));
  }
}
