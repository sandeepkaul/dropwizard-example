/**
 * 
 */
package com.sandeepkaul.dropwizard.service;

import com.sandeepkaul.dropwizard.db.DBUtil;

/**
 * @author sandeep kaul
 *
 */
public class MyService {

  private final DBUtil dbUtil;
  public MyService(DBUtil dbUtil) {
    this.dbUtil = dbUtil;
  }
  public String get() {
    return dbUtil.getResponse();
  }
}
