package com.amp.accountmanagement.service.error;

public class ResourceNotFoundException extends RuntimeException {

  private static final String MSG_TEMPLATE = "%s resource with id %s doesn't exists.";

  public ResourceNotFoundException(String resourceId, String resourceName) {
    super(String.format(MSG_TEMPLATE, resourceName, resourceId));
  }

  public ResourceNotFoundException(String resourceId, String resourceName, Throwable cause) {
    super(String.format(MSG_TEMPLATE, resourceName, resourceId), cause);
  }

  public ResourceNotFoundException(Long resourceId, String resourceName) {
    this(String.valueOf(resourceId), resourceName);
  }

  public ResourceNotFoundException(Long resourceId, String resourceName, Throwable cause) {
    this(String.valueOf(resourceId), resourceName, cause);
  }
}
