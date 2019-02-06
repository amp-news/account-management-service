package com.amp.accountmanagement.service.converter;

public interface Converter<O, DTO> {

  DTO toResource(O entity);

  O toEntity(DTO dto);
}
