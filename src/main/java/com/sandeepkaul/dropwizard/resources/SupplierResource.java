/**
 * 
 */
package com.sandeepkaul.dropwizard.resources;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.sandeepkaul.dropwizard.model.Product;
import com.sandeepkaul.dropwizard.model.PurchaseModel;
import com.sandeepkaul.dropwizard.service.CashService;
import com.sandeepkaul.dropwizard.service.ProductService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sandeep kaul
 *
 */
@Path("/supplier")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SupplierResource {

  private final ProductService productService;
  private final CashService cashService;

  public SupplierResource(ProductService productService, CashService cashService) {
    this.productService = productService;
    this.cashService = cashService;
  }

  @POST
  @Path("/product/add")
  @Timed
  @Metered(name = "addNewProduct.CallMeter")
  @ExceptionMetered(name = "addNewProduct.ExceptionMeter", cause = Exception.class)
  public Product addNewProduct(@Context HttpServletRequest request, @NotNull Product product) {

    return productService.addNewProduct(product);
  }

  @GET
  @Path("/product")
  @Timed
  @Metered(name = "getProducts.CallMeter")
  @ExceptionMetered(name = "getProducts.ExceptionMeter", cause = Exception.class)
  public List<Product> getProducts(@Context HttpServletRequest request,
      @QueryParam("size") @DefaultValue("100") int size,
      @QueryParam("page") @DefaultValue("1") int page) {

    return productService.getProducts(page, size);
  }

  @POST
  @Path("/product/change_price/{productId}/{newPrice}")
  @Timed
  @Metered(name = "changePrice.CallMeter")
  @ExceptionMetered(name = "changePrice.ExceptionMeter", cause = Exception.class)
  public void changePrice(@Context HttpServletRequest request,
      @NotNull @PathParam("productId") Integer productId,
      @NotNull @PathParam("newPrice") Integer price) {

    productService.changePrice(productId, price);
  }


  @POST
  @Path("/product/change_quantity/{productId}/{newQuantity}")
  @Timed
  @Metered(name = "changeQuantity.CallMeter")
  @ExceptionMetered(name = "changeQuantity.ExceptionMeter", cause = Exception.class)
  public void changeQuantity(@Context HttpServletRequest request,
      @NotNull @PathParam("productId") Integer productId,
      @NotNull @PathParam("newQuantity") Integer quantity) {

    productService.changeQuantity(productId, quantity);
  }


  @POST
  @Path("/cash/add/{amount}")
  @Timed
  @Metered(name = "addCash.CallMeter")
  @ExceptionMetered(name = "addCash.ExceptionMeter", cause = Exception.class)
  public double addCash(@Context HttpServletRequest request,
      @NotNull @PathParam("amount") Double amount) {

    return cashService.add(amount);
  }

  @POST
  @Path("/cash/remove/{amount}")
  @Timed
  @Metered(name = "removeCash.CallMeter")
  @ExceptionMetered(name = "removeCash.ExceptionMeter", cause = Exception.class)
  public double removeCash(@Context HttpServletRequest request,
      @NotNull @PathParam("amount") Double amount) {

    return cashService.remove(amount);
  }


  @GET
  @Path("/statement")
  @Timed
  @Metered(name = "getStatement.CallMeter")
  @ExceptionMetered(name = "getStatement.ExceptionMeter", cause = Exception.class)
  public List<PurchaseModel> getStatement(@Context HttpServletRequest request,
      @QueryParam("size") @DefaultValue("100") int size,
      @QueryParam("page") @DefaultValue("1") int page) {

    return productService.getStatement(size, page);
  }
  
  @POST
  @Path("/machine/reset")
  @Timed
  @Metered(name = "resetMachine.CallMeter")
  @ExceptionMetered(name = "resetMachine.ExceptionMeter", cause = Exception.class)
  public void resetMachine(@Context HttpServletRequest request) {

     productService.resetMachine();
     double cash  = cashService.getSystemCash();
     cashService.remove(cash);
  }

}
