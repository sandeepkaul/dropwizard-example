/**
 * 
 */
package com.sandeepkaul.dropwizard.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import lombok.extern.slf4j.Slf4j;

/**
 * @author sandeep kaul
 *
 */
@Slf4j
public class DBUtil {
  private final DataSource dataSource;
  
  public DBUtil(DataSource dataSource) {
    this.dataSource = dataSource;
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

  private void closeAll(final ResultSet rs, final PreparedStatement preparedStatement, final Connection connection) {
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
}
