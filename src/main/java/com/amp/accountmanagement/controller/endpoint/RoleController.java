package com.amp.accountmanagement.controller.endpoint;

import java.util.Collection;
import javax.validation.Valid;

import com.amp.accountmanagement.model.dto.RoleTO;
import com.amp.accountmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {

  private RoleService service;

  @Autowired
  public RoleController(RoleService roleService) {
    this.service = roleService;
  }

  @GetMapping()
  public Collection<RoleTO> retrieveAll() {
    return service.retrieveAll();
  }

  @GetMapping("/{id}")
  public RoleTO retrieve(@PathVariable Long id) {
    return service.retrieve(id);
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> save(@Valid @RequestBody RoleTO role) {
    return ResponseEntity.created(
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(service.save(role))
            .toUri())
        .build();
  }

  @PutMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void update(@Valid @RequestBody RoleTO role, @PathVariable Long id) {
    role.setRoleId(id);
    service.update(role);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void delete(@PathVariable(value = "id") Long id) {
    service.delete(id);
  }
}
