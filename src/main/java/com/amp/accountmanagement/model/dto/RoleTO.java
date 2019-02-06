package com.amp.accountmanagement.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
@JsonPropertyOrder({"id", "name", "description"})
public class RoleTO extends ResourceSupport {

  public static final String RESOURCE_NAME = "Role";

  @JsonProperty("id")
  @Size(max = 100)
  private Long roleId;

  @NotEmpty
  @Size(max = 1000)
  private String name;

  @NotNull
  private String description;
}
