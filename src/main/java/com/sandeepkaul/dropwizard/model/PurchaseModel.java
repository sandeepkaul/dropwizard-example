/**
 * 
 */
package com.sandeepkaul.dropwizard.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sandeep Kaul
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseModel {

  private int id;
  private Map<Integer, Integer> productQuantity;
  private String userId;
  private double cashPaid;
  private boolean reverted;
}
