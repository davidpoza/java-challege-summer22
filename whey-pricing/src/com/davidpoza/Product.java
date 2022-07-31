package com.davidpoza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Product {
  private Connection con;
  private int id;
  private String name;
  private String url;
  private String brand;
  private Double kg;
  private ArrayList<Price> prices;
  
  public enum Brand {
    MYPROTEIN,
    HSN,
    PROZIS,
    BULKPOWDERS
  }
  
  public Product(Connection con, int id, String name, String url, String brand, Double kg) {
    super();
    this.con = con;
    this.name = name;
    this.url = url;
    this.brand = brand;  
    this.id = id;
    this.setKg(kg);
    this.prices = new ArrayList<Price>();
    this.readPrices();
  }

  public void readPrices() {
    this.prices.clear();
    try {
      PreparedStatement statement = con.prepareStatement("SELECT date, amount, discount, product_id FROM prices_tbl WHERE product_id = ? ORDER BY date ASC");
      statement.setInt(1, this.id);
      statement.execute();
      ResultSet rs = statement.getResultSet();
      while(rs.next()) {
        Price p = null;
        p = new PriceFactory(con, this.brand, this.url, LocalDateTime.parse(rs.getString("date")), rs.getDouble("amount"), this).getPrice();
        this.prices.add(p);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }          
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

  public Double getKg() {
    return kg;
  }

  public void setKg(Double kg) {
    this.kg = kg;
  }
  
}
