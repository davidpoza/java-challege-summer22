package com.davidpoza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Product {
  private Connection con;
  private int id;
  private String name;
  private String url;
  private String brand;
  private ArrayList<Price> prices;
  
  public enum Brand {
    MYPROTEIN,
    HSN,
    PROZIS,
    BULKPOWDERS
  }
  
  public Product(Connection con, int id, String name, String url, String brand) {
    super();
    this.con = con;
    this.name = name;
    this.url = url;
    this.brand = brand;  
    this.id = id;
    this.readPrices();
  }

  private ArrayList<Price> readPrices() {
    
    try {
      PreparedStatement statement = con.prepareStatement("SELECT date, amount, discount, product_id FROM prices_tbl");
      statement.execute();
      ResultSet rs = statement.getResultSet();
      Price p = null;
      while(rs.next()) {
        switch(Brand.valueOf(this.brand)) {
          case MYPROTEIN:
            p = new MyProteinPrice(con, this.url, rs.getTimestamp("date").toLocalDateTime(), rs.getDouble("amount"), this);
            break;
          case HSN:
            p = new HsnPrice(con, this.url, rs.getTimestamp("date").toLocalDateTime(), rs.getDouble("amount"), this);
            break;
          case PROZIS:
            break;
          case BULKPOWDERS:
            break;
        }
        this.prices.add(p);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
          
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public ArrayList<Price> getPrices() {
    return prices;
  }

  public void setPrices(ArrayList<Price> prices) {
    this.prices = prices;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  
}
