package com.amp.accountmanagement.repository;

import java.util.Optional;

import com.amp.accountmanagement.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findRoleByName(String name);

  boolean existsByName(String name);
}
