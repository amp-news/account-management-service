package com.amp.accountmanagement.model.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.Identifiable;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"accounts"})
@ToString(exclude = {"accounts"})
public class Role implements Identifiable<Long> {

  @Id
  @SequenceGenerator(name = "roleSeq", sequenceName = "role_id_seq")
  @GeneratedValue(strategy = SEQUENCE, generator = "roleSeq")
  private Long id;

  private String name;
  private String description;

  @OneToMany(mappedBy = "role", cascade = ALL)
  private Set<Account> accounts;
}
