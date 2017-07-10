package com.sandeepkaul.dropwizard.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sandeepkaul.dropwizard.config.DataBaseConfig;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @author sandeep kaul
 *
 */
@Slf4j
public class DataSource implements Managed {

  private ComboPooledDataSource dbCpds;


  /**
   * Data source constructor. 
   */

  public DataSource(final DataBaseConfig dbConfig)
      throws IOException, SQLException, PropertyVetoException {
    dbCpds = new ComboPooledDataSource();
    dbCpds.setDriverClass("com.mysql.jdbc.Driver"); // loads the jdbc driver
    dbCpds.setJdbcUrl(dbConfig.getDbUrl());
    dbCpds.setUser(dbConfig.getDbUser());
    dbCpds.setPassword(dbConfig.getDbPassword());
    dbCpds.setTestConnectionOnCheckin(dbConfig.getTestConnectionOnCheckin());
    dbCpds.setIdleConnectionTestPeriod(dbConfig.getIdleConnectionTestPeriod());
    dbCpds.setTestConnectionOnCheckout(dbConfig.getTestConnectionOnCheckout());

    // the settings below are optional -- c3p0 can work with defaults
    dbCpds.setMinPoolSize(5);
    dbCpds.setAcquireIncrement(5);
    dbCpds.setMaxPoolSize(20);
    dbCpds.setMaxStatements(180);

    

  }

  /**
   * Get DBConnection.
   */
  public Connection getDBConnection() throws SQLException {
    return dbCpds.getConnection();
  }



  public void start() throws Exception {

  }

  public void stop() throws Exception {
    dbCpds.close();
  }
}
