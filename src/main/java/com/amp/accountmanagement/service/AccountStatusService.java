package com.amp.accountmanagement.service;

import java.util.Set;

import com.amp.accountmanagement.model.dto.AccountStatusTO;

public interface AccountStatusService {

  Set<AccountStatusTO> retrieveAll();
}
