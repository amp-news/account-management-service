package com.amp.accountmanagement.service.validation;

public interface Validator<T> {

  void validate(T entity);
}
