package com.amp.accountmanagement.controller.endpoint;

import java.util.Set;

import com.amp.accountmanagement.model.dto.AccountStatusTO;
import com.amp.accountmanagement.service.AccountStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/statuses")
public class AccountStatusController {

  private AccountStatusService service;

  @Autowired
  public AccountStatusController(AccountStatusService accountService) {
    this.service = accountService;
  }

  @GetMapping
  public Set<AccountStatusTO> retrieveAll() {
    return service.retrieveAll();
  }
}
