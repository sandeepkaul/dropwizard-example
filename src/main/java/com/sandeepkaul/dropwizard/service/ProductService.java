/**
 * 
 */
package com.sandeepkaul.dropwizard.service;

import java.util.List;

import com.sandeepkaul.dropwizard.db.DBUtil;
import com.sandeepkaul.dropwizard.model.Product;
import com.sandeepkaul.dropwizard.model.PurchaseModel;

/**
 * @author sandeep.kaul
 *
 */
public class ProductService {

  private final DBUtil dbUtil;

  public ProductService(DBUtil dbUtil) {
    this.dbUtil = dbUtil;
  }

  public Product addNewProduct(Product product) {

    return dbUtil.addNewProduct(product);
  }

  public List<Product> getProducts(int page, int size) {
    
    int offset = (page - 1) * size;
    return dbUtil.getProducts(size, offset);
  }

  public void changePrice(int productId, int price) {
    dbUtil.changePrice(productId,price );
  }

  public void changeQuantity(int productId, int quantity) {
    dbUtil.changeQuantity(productId,quantity );
  }

  public List<PurchaseModel> getStatement(int size, int page) {
    int offset = (page - 1) * size;
    return dbUtil.getPurchaseStatement(size, offset);
  }

  public void resetMachine() {
    
    List<Product> products = getProducts(1, 1000);
    if(products != null && !products.isEmpty()){
      for (Product product : products) {
        changeQuantity(product.getId(), 0);
      }
    }
      
  }
}
