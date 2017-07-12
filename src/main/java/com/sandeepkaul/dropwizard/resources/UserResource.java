/**
 * 
 */
package com.sandeepkaul.dropwizard.resources;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.sandeepkaul.dropwizard.model.Product;
import com.sandeepkaul.dropwizard.model.PurchaseModel;
import com.sandeepkaul.dropwizard.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sandeep kaul
 *
 */
@Path("/user")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

  private final UserService userService;

  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @POST
  @Path("/purchase")
  @Timed
  @Metered(name = "purchase.CallMeter")
  @ExceptionMetered(name = "purchase.ExceptionMeter", cause = Exception.class)
  public double purchase(@Context HttpServletRequest request, @NotNull PurchaseModel purchaseModel) {

    log.info("Initiating purchase for purchaseModel:{}", purchaseModel);
    return userService.purchase(purchaseModel);
  }
  
  @POST
  @Path("/purchase/cancel/{purchaseId}")
  @Timed
  @Metered(name = "purchase.CallMeter")
  @ExceptionMetered(name = "purchase.ExceptionMeter", cause = Exception.class)
  public double cancelPurchase(@Context HttpServletRequest request, @PathParam("purchaseId") Integer purchaseId) {

    return userService.cancelPurchase(purchaseId);
  }

}
