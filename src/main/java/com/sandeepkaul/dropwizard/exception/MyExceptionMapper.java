/**
 * 
 */
package com.sandeepkaul.dropwizard.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Sandeep Kaul
 *
 */
@Provider
public class MyExceptionMapper implements ExceptionMapper<RuntimeException>{

  public Response toResponse(RuntimeException exception) {
    
    return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\""+exception.getMessage()+"\"}")
        .type(MediaType.APPLICATION_JSON).build();
  }

}
