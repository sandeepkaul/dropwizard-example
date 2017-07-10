package com.sandeepkaul.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 
 * @author sandeep kaul
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataBaseConfig {
  @NotEmpty
  @NotNull
  private String dbUrl;
  @NotEmpty
  @NotNull
  private String dbUser;
  @NotEmpty
  @NotNull
  private String dbPassword;
  @NotNull
  private Integer idleConnectionTestPeriod;
  @NotNull
  private Boolean testConnectionOnCheckin;
  @NotNull
  private Boolean testConnectionOnCheckout;
}
