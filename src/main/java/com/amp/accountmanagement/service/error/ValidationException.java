package com.amp.accountmanagement.service.error;

public class ValidationException extends RuntimeException {

  private static final String MSG_TEMPLATE = "%s resource is not valid. ";

  public ValidationException(String resourceName, String message) {
    super(String.format(MSG_TEMPLATE, resourceName) + message);
  }

  public ValidationException(String resourceName, String message, Throwable cause) {
    super(String.format(MSG_TEMPLATE, resourceName) + message, cause);
  }
}
