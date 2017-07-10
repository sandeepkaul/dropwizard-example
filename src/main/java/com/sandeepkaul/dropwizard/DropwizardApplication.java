package com.sandeepkaul.dropwizard;



import com.sandeepkaul.dropwizard.db.DBUtil;
import com.sandeepkaul.dropwizard.db.DataSource;
import com.sandeepkaul.dropwizard.resources.MyResource;
import com.sandeepkaul.dropwizard.service.MyService;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * 
 * @author sandeep kaul
 *
 */
public class DropwizardApplication extends Application<DropwizardConfiguration> 
{
   
  public static void main(String[] args) throws Exception {
    new DropwizardApplication().run(args);
  }

    @Override
    public void run(DropwizardConfiguration configuration, Environment environment) throws Exception {

      //DB
      DataSource dataSource = new DataSource(configuration.getDatabaseConfig());
      environment.lifecycle().manage(dataSource);
      
      DBUtil dbUtil = new DBUtil(dataSource);
      
      //Services
      MyService myService = new MyService(dbUtil);
      
      //Resources
      MyResource myResource = new MyResource(myService);
      
      environment.jersey().register(myResource);
    }
}
