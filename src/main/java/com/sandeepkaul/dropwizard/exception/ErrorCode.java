/**
 * 
 */
package com.sandeepkaul.dropwizard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Sandeep Kaul
 *
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

  INVALID_DATA(1001, 400, "Invalid Data");
  
  private int statusCode;
  private int httpCode;
  private String errorMessage;
}
