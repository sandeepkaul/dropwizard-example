package com.sandeepkaul.dropwizard;



import com.sandeepkaul.dropwizard.db.DBUtil;
import com.sandeepkaul.dropwizard.db.DataSource;
import com.sandeepkaul.dropwizard.exception.MyExceptionMapper;
import com.sandeepkaul.dropwizard.resources.SupplierResource;
import com.sandeepkaul.dropwizard.resources.UserResource;
import com.sandeepkaul.dropwizard.service.CashService;
import com.sandeepkaul.dropwizard.service.ProductService;
import com.sandeepkaul.dropwizard.service.UserService;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * 
 * @author sandeep kaul
 *
 */
public class DropwizardApplication extends Application<DropwizardConfiguration> {

  public static void main(String[] args) throws Exception {
    new DropwizardApplication().run(args);
  }

  @Override
  public void run(DropwizardConfiguration configuration, Environment environment) throws Exception {

    // DB
    DataSource dataSource = new DataSource(configuration.getDatabaseConfig());
    environment.lifecycle().manage(dataSource);
    DBUtil dbUtil = new DBUtil(dataSource);

    // Services
    ProductService productService = new ProductService(dbUtil);
    UserService userService = new UserService(dbUtil);
    CashService cashService = new CashService(dbUtil);

    // Resources
    SupplierResource supplierResource = new SupplierResource(productService, cashService);
    UserResource userResource = new UserResource(userService);
    
    // Resource Registration
    environment.jersey().register(supplierResource);
    environment.jersey().register(userResource);
    
    //Exception Mapper
    environment.jersey().register(new MyExceptionMapper());
  }
}
