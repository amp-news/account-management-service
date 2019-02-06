package com.amp.accountmanagement.controller.endpoint;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

import com.amp.accountmanagement.model.dto.AccountPartialInputTO;
import com.amp.accountmanagement.model.dto.AccountTO;
import com.amp.accountmanagement.model.dto.AccountTOSecured;
import com.amp.accountmanagement.model.dto.PageTO;
import com.amp.accountmanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = "/users")
public class AccountController {

  private AccountService service;

  @Autowired
  public AccountController(AccountService accountService) {
    this.service = accountService;
  }

  @GetMapping("/secure")
  public List<AccountTOSecured> retrieveAllSecured(
      @RequestParam(name = "search") String filterString) {
    return service.retrieveAllSecured(new String(Base64.getUrlDecoder().decode(filterString)));
  }

  @GetMapping
  public PageTO<AccountTO> retrieveAll(
      @RequestParam(name = "search", required = false) String filterString, Pageable pageable) {
    return Objects.isNull(filterString)
        ? service.retrieveAll(pageable)
        : service.retrieveAll(new String(Base64.getUrlDecoder().decode(filterString)), pageable);
  }

  @GetMapping("/{id}")
  public AccountTO retrieve(@PathVariable Long id) {
    return service.retrieve(id);
  }

  @PostMapping("/secure")
  public ResponseEntity<?> save(@Valid @RequestBody AccountTOSecured account) {
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(service.save(account))
                .toUri())
        .build();
  }

  @PatchMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  public void updatePartial(
      @Valid @RequestBody AccountPartialInputTO account, @PathVariable Long id) {
    account.setAccountId(id);
    service.update(account);
  }

  @PutMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  public void update(@Valid @RequestBody AccountTO account, @PathVariable Long id) {
    account.setAccountId(id);
    service.update(account);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void delete(@PathVariable(value = "id") Long id) {
    service.delete(id);
  }
}
