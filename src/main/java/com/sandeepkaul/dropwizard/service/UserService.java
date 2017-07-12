/**
 * 
 */
package com.sandeepkaul.dropwizard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.RuntimeErrorException;

import com.sandeepkaul.dropwizard.db.DBUtil;
import com.sandeepkaul.dropwizard.model.Product;
import com.sandeepkaul.dropwizard.model.PurchaseModel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sandeep kaul
 *
 */
@Slf4j
public class UserService {

  private final DBUtil dbUtil;

  public UserService(DBUtil dbUtil) {
    this.dbUtil = dbUtil;
  }

  public double cancelPurchase(Integer purchaseId) {
    
    PurchaseModel purchase = dbUtil.getPurchaseById(purchaseId);
    if(purchase == null) {
      throw new RuntimeException("Invald Purchase ID: "+purchaseId);
    }
    double systemCash = dbUtil.getSystemCash();
    double purchaseAmount = purchase.getCashPaid();
    if(systemCash < purchaseAmount) {
      throw new RuntimeException("Nt Enough money left to refund");
    }
    dbUtil.cancelPurchase(purchaseId, purchase);
    return 0;
  }
  
  public double purchase(PurchaseModel purchaseModel) {

    if (purchaseModel == null || purchaseModel.getCashPaid() == 0
        || purchaseModel.getProductQuantity() == null
        || purchaseModel.getProductQuantity().size() == 0 || purchaseModel.getUserId() == null) {
      throw new RuntimeException("Invalid Data");
    }

    List<Integer> productIds = getProductIdsForPurchase(purchaseModel);
    log.info("Request for purchase on productIds:{} ", productIds);
    
    Map<Integer, Product> productInformation  = dbUtil.getProductInformation(productIds);
    log.info("Product Information:{} ", productInformation);
    
    double totalAmount  = checkQuantityAndPrice(productInformation, purchaseModel, productIds);
    double balance = purchaseModel.getCashPaid() - totalAmount;
    dbUtil.purchase(productIds, purchaseModel,totalAmount);
    return balance;
  }

  

  private double checkQuantityAndPrice(Map<Integer, Product> productInformation, PurchaseModel purchaseModel,
      List<Integer> productIds) {
    
    double totalAmount = 0;
    Map<Integer, Integer> requestMap = purchaseModel.getProductQuantity();
    for (Integer productId : productIds) {
      int requestedQuantity = requestMap.get(productId);
      Product product = productInformation.get(productId);
      if(product == null) {
        throw new RuntimeException("Incorrect Product Id: "+productId);
      }
      int availableQuantity = productInformation.get(productId).getQuantity();
      if(requestedQuantity > availableQuantity) {
        throw new RuntimeException("Quantity not available for product:"+productId);
      }
      totalAmount = totalAmount+(requestedQuantity*product.getPrice());
    }
    
    if(totalAmount > purchaseModel.getCashPaid()) {
      throw new RuntimeException("Less Money Paid. Total Amount:"+totalAmount);
    }
    return totalAmount;
  }

  private List<Integer> getProductIdsForPurchase(PurchaseModel purchaseModel) {
    List<Integer> productIds = new ArrayList<Integer>();
    for (Entry<Integer, Integer> entry : purchaseModel.getProductQuantity().entrySet()) {
      productIds.add(entry.getKey());
    }
    return productIds;
  }

  
}
