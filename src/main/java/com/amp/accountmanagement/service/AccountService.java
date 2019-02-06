package com.amp.accountmanagement.service;

import java.util.List;
import javax.validation.constraints.NotNull;

import com.amp.accountmanagement.model.dto.AccountPartialInputTO;
import com.amp.accountmanagement.model.dto.AccountTO;
import com.amp.accountmanagement.model.dto.AccountTOSecured;
import com.amp.accountmanagement.model.dto.PageTO;
import org.springframework.data.domain.Pageable;

public interface AccountService {

  List<AccountTOSecured> retrieveAllSecured(@NotNull String filter);

  PageTO<AccountTO> retrieveAll(@NotNull Pageable pageable);

  PageTO<AccountTO> retrieveAll(@NotNull String filter, @NotNull Pageable pageable);

  AccountTO retrieve(@NotNull Long id);

  Long save(@NotNull AccountTOSecured accountTO);

  void update(@NotNull AccountTO accountTO);

  void update(@NotNull AccountPartialInputTO accountPartialInputTO);

  void delete(@NotNull Long id);
}
