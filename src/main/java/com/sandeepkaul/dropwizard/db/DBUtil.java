/**
 * 
 */
package com.sandeepkaul.dropwizard.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandeepkaul.dropwizard.model.Product;
import com.sandeepkaul.dropwizard.model.PurchaseModel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sandeep kaul
 *
 */
@Slf4j
public class DBUtil {
  private final DataSource dataSource;
  private final ObjectMapper objectMapper;

  public DBUtil(DataSource dataSource) {
    this.dataSource = dataSource;
    objectMapper = new ObjectMapper();
  }

  public void addCash(Double amount) {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String sql = "UPDATE system_cash SET system_cash=system_cash+? where id=1";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setDouble(1, amount);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
  }

  public double getSystemCash() {

    double cash = 0;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String sql = "SELECT * from system_cash where id=1";
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        cash = resultSet.getDouble("system_cash");
      }

    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
    return cash;
  }

  public void removeCash(Double amount) {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String sql = "UPDATE system_cash SET system_cash=system_cash-? where id=1";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setDouble(1, amount);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
  }

  public void cancelPurchase(Integer purchaseId, PurchaseModel purchase) {
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      connection.setAutoCommit(false);

      // Update Purchase
      String sql = "UPDATE purchases SET reverted=true where id=?;";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, purchaseId);
      preparedStatement.executeUpdate();
      closePreparedStatement(preparedStatement);

      // Reduce system cash
      sql = "UPDATE system_cash SET system_cash=system_cash-? where id=1";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setDouble(1, purchase.getCashPaid());
      preparedStatement.executeUpdate();
      closePreparedStatement(preparedStatement);

      // Increase Quantity
      for (Entry<Integer, Integer> entry : purchase.getProductQuantity().entrySet()) {
        int productCount = new Integer(entry.getValue());
        log.info("entry.getKey():{}",entry.getKey());
        int productId = new Integer(entry.getKey()+"");
        sql = "UPDATE products SET quantity=quantity+? where id=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, productCount);
        preparedStatement.setInt(2, productId);
        preparedStatement.executeUpdate();
        closePreparedStatement(preparedStatement);
      }
    } catch (SQLException e) {
      log.error("SQL Error ", e);
      rollback(connection);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      rollback(connection);
      throw new RuntimeException("Something went wrong");
    } finally {
      commit(connection);
      closeAll(resultSet, preparedStatement, connection);
    }
  }
  
  public void purchase(List<Integer> productIds, PurchaseModel purchaseModel, double totalAmount) {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      connection.setAutoCommit(false);

      // Insert Purchase
      String sql = "INSERT INTO purchases(`user_id`,`products`,`amount`) VALUES (?,?,?);";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, purchaseModel.getUserId());
      preparedStatement.setString(2,
          objectMapper.writeValueAsString(purchaseModel.getProductQuantity()));
      preparedStatement.setDouble(3, totalAmount);
      preparedStatement.executeUpdate();
      closePreparedStatement(preparedStatement);

      // Increase system cash
      sql = "UPDATE system_cash SET system_cash=system_cash+? where id=1";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setDouble(1, totalAmount);
      preparedStatement.executeUpdate();
      closePreparedStatement(preparedStatement);

      // Reduce Quantity
      for (Integer productId : productIds) {
        int productCount = purchaseModel.getProductQuantity().get(productId);
        sql = "UPDATE products SET quantity=quantity-? where id=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, productCount);
        preparedStatement.setInt(2, productId);
        preparedStatement.executeUpdate();
        closePreparedStatement(preparedStatement);
      }


    } catch (SQLException e) {
      log.error("SQL Error ", e);
      rollback(connection);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      rollback(connection);
      throw new RuntimeException("Something went wrong");
    } finally {
      commit(connection);
      closeAll(resultSet, preparedStatement, connection);
    }
  }

  public void changeQuantity(int productId, int quantity) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String selectSql = "UPDATE products set quantity=? where id=?;";
      preparedStatement = connection.prepareStatement(selectSql);
      preparedStatement.setDouble(1, quantity);
      preparedStatement.setInt(2, productId);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
  }

  public void changePrice(int productId, int price) {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String selectSql = "UPDATE products set price=? where id=?;";
      preparedStatement = connection.prepareStatement(selectSql);
      preparedStatement.setDouble(1, price);
      preparedStatement.setInt(2, productId);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
  }

  public PurchaseModel getPurchaseById(Integer purchaseId) {
    
    PurchaseModel purchaseModel = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String selectSql = "SELECT * from purchases where id =?;";
      preparedStatement = connection.prepareStatement(selectSql);
      preparedStatement.setInt(1, purchaseId);

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String userId = resultSet.getString("user_id");
        String products = resultSet.getString("products");
        boolean reverted = resultSet.getBoolean("reverted");
        double amount = resultSet.getDouble("amount");
        purchaseModel = new PurchaseModel(id, objectMapper.readValue(products, Map.class), userId, amount, reverted);
      }

    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
    return purchaseModel;
    
  }
  
  public List<PurchaseModel> getPurchaseStatement(int limit, int offset) {
    
    List<PurchaseModel> response = new ArrayList<PurchaseModel>();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String selectSql = "SELECT * from purchases order by id desc limit ? offset ?;";
      preparedStatement = connection.prepareStatement(selectSql);
      preparedStatement.setInt(1, limit);
      preparedStatement.setInt(2, offset);

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String userId = resultSet.getString("user_id");
        String products = resultSet.getString("products");
        boolean reverted = resultSet.getBoolean("reverted");
        double amount = resultSet.getDouble("amount");
        PurchaseModel purchaseModel = new PurchaseModel(id, objectMapper.readValue(products, Map.class), userId, amount, reverted);
        log.info("purchaseModel:{}",purchaseModel);
        response.add(purchaseModel);
      }

    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
    return response;
  }

  
  public List<Product> getProducts(int limit, int offset) {

    List<Product> respose = new ArrayList<Product>();

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String selectSql = "SELECT * from products order by id desc limit ? offset ?;";
      preparedStatement = connection.prepareStatement(selectSql);
      preparedStatement.setInt(1, limit);
      preparedStatement.setInt(2, offset);

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        int quantity = resultSet.getInt("quantity");
        double price = resultSet.getDouble("price");
        Product product = new Product(id, name, description, quantity, price);
        respose.add(product);
      }

    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
    return respose;
  }

  public Product addNewProduct(Product product) {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String selectSql =
          "INSERT INTO products(`name`,`description`,`quantity`,`price`) VALUES (?,?,?,?);";
      preparedStatement = connection.prepareStatement(selectSql);
      preparedStatement.setString(1, product.getName());
      preparedStatement.setString(2, product.getDescription());
      preparedStatement.setInt(3, product.getQuantity());
      preparedStatement.setDouble(4, product.getPrice());

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
    return product;
  }

  public String getResponse() {

    String response = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String selectSql = "SELECT *  FROM api_response WHERE name = ?";
      preparedStatement = connection.prepareStatement(selectSql);
      preparedStatement.setString(1, "api");
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        response = resultSet.getString("response");
      }
    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
    return response;
  }

  private void closeAll(final ResultSet rs, final PreparedStatement preparedStatement,
      final Connection connection) {
    try {
      if (rs != null && !rs.isClosed()) {
        rs.close();
      }
    } catch (Exception e) {
      log.error("Failed to close result set.");
    }
    try {
      if (preparedStatement != null && !preparedStatement.isClosed()) {
        preparedStatement.close();
      }
    } catch (Exception e) {
      log.error("Failed to close preparedStatement.");
    }
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (Exception e) {
      log.error("Failed to close database connection.");
    }
  }

  private void closePreparedStatement(final PreparedStatement preparedStatement) {
    try {
      if (preparedStatement != null && !preparedStatement.isClosed()) {
        preparedStatement.close();
      }
    } catch (Exception e) {
      log.error("Failed to close preparedStatement.");
    }
  }

  public Map<Integer, Product> getProductInformation(List<Integer> productIds) {

    log.info("getProductInformation called with productIds:{}", productIds);
    Map<Integer, Product> response = new HashMap<Integer, Product>();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getDBConnection();
      String selectSql =
          "SELECT * from products where id in (" + StringUtils.join(productIds, ",") + ")";
      preparedStatement = connection.prepareStatement(selectSql);
      log.info("Running Query:{}", preparedStatement);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        int quantity = resultSet.getInt("quantity");
        double price = resultSet.getDouble("price");
        Product product = new Product(id, name, description, quantity, price);
        response.put(id, product);
      }

    } catch (SQLException e) {
      log.error("SQL Error ", e);
      throw new RuntimeException("Something went wrong");
    } catch (Exception e) {
      log.error("Exception ", e);
      throw new RuntimeException("Something went wrong");
    } finally {
      closeAll(resultSet, preparedStatement, connection);
    }
    log.info("getProductInformation responded with response:{}", response);
    return response;
  }


  private void rollback(Connection connection) {
    try {
      connection.rollback();
    } catch (SQLException e) {
      log.error("Not able to rollback", e);
    }
  }

  private void commit(Connection connection) {
    try {
      connection.commit();
    } catch (SQLException e) {
      log.error("Not able to commit", e);
    }
  }


  

  


}
