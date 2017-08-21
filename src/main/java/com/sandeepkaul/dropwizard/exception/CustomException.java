/**
 * 
 */
package com.sandeepkaul.dropwizard.exception;

import lombok.Getter;

/**
 * @author Sandeep Kaul
 *
 */
@Getter
public class CustomException extends Exception{

  private ErrorCode errorCode;
  
  private static final long serialVersionUID = -3853689944157298314L;

  public CustomException(final ErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
  }
}
