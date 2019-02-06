package com.amp.accountmanagement.service.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

public abstract class BaseConverter<O extends Identifiable<?>, DTO extends ResourceSupport>
    extends IdentifiableResourceAssemblerSupport<O, DTO> implements Converter<O, DTO> {

  private ModelMapper modelMapper;

  protected Class<O> oType;
  protected Class<DTO> dtoType;

  public BaseConverter(Class<?> controller, Class<O> oType, Class<DTO> dtoType) {
    super(controller, dtoType);
    this.oType = oType;
    this.dtoType = dtoType;
    this.modelMapper = new ModelMapper();
    this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    this.addCustomMappings(modelMapper);
  }

  @Override
  public O toEntity(DTO dto) {
    return modelMapper.map(dto, oType);
  }

  @Override
  public DTO toResource(O entity) {
    DTO resource = createResource(entity);
    modelMapper.map(entity, resource);
    addCustomLinks(resource);
    return resource;
  }

  protected void addCustomMappings(ModelMapper modelMapper) {
  }

  protected void addCustomLinks(DTO resource) {
  }
}
