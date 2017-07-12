/**
 * 
 */
package com.sandeepkaul.dropwizard.service;

import com.sandeepkaul.dropwizard.db.DBUtil;

/**
 * @author Sandeep Kaul
 *
 */
public class CashService {

  private final DBUtil dbUtil;

  public CashService(DBUtil dbUtil) {
    this.dbUtil = dbUtil;
  }

  public double getSystemCash() {
    return dbUtil.getSystemCash();
  }
  public double add(Double amount) {
   
    dbUtil.addCash(amount);
    return dbUtil.getSystemCash();
    
  }

  public double remove(Double amount) {
    
    double cash = dbUtil.getSystemCash();
    if(amount> cash) {
      throw new RuntimeException("Not enough money evailable. Current Balance: "+cash);
    }
    dbUtil.removeCash(amount);
    return dbUtil.getSystemCash();
  }
}
