/**
 * 
 */
package com.sandeepkaul.dropwizard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sandeep.kaul
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

  private int id;
  private String name;
  private String description;
  private int quantity;
  private double price;
  
}
