package com.amp.accountmanagement.repository;

import java.util.Optional;

import com.amp.accountmanagement.model.entity.Account;
import com.amp.accountmanagement.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

  Optional<Account> findAccountByEmail(String email);

  boolean existsByRole(Role role);
}
