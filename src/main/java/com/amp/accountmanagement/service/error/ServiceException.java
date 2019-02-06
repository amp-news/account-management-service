package com.amp.accountmanagement.service.error;

public class ServiceException extends RuntimeException {

  private static final String MSG_TEMPLATE = "Error occurred during resource processing. ";

  public ServiceException(String message) {
    super(MSG_TEMPLATE + message);
  }

  public ServiceException(String message, Throwable cause) {
    super(MSG_TEMPLATE + message, cause);
  }
}
