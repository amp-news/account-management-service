package com.amp.accountmanagement.model.entity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import org.springframework.hateoas.Identifiable;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
public class Account implements Identifiable<Long> {

  @Id
  @SequenceGenerator(name = "accountSeq", sequenceName = "account_id_seq")
  @GeneratedValue(strategy = SEQUENCE, generator = "accountSeq")
  private Long id;

  private String nickname;
  private String password;
  private String firstName;
  private String lastName;
  private String email;

  @Enumerated(STRING)
  private AccountStatus status;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "role_id")
  private Role role;
}
