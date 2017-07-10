/**
 * 
 */
package com.sandeepkaul.dropwizard.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.sandeepkaul.dropwizard.service.MyService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sandeep kaul
 *
 */
@Path("/api")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MyResource {

  private final MyService myService;

  public MyResource(MyService myService) {
    this.myService = myService;
  }

  @GET
  @Timed
  @Metered(name = "get.CallMeter")
  @ExceptionMetered(name = "get.ExceptionMeter", cause = Exception.class)
  public String get(@Context HttpServletRequest request) {

    return myService.get();
  }
}
