package com.sandeepkaul.dropwizard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sandeepkaul.dropwizard.config.DataBaseConfig;

import io.dropwizard.Configuration;
import lombok.Getter;

/**
 * 
 * @author sandeep kaul
 *
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DropwizardConfiguration extends Configuration {

  private DataBaseConfig databaseConfig;
}
