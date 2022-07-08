package com.davidpoza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PriceChart {
  private Connection con;
  private ArrayList<Price> prices = new ArrayList<Price>();
  private ArrayList<Product> products = new ArrayList<Product>();
  
  public  PriceChart(Connection con, LocalDateTime from, LocalDateTime to) {
    super();
    this.con = con;
    this.getAllProducts();
  }
  
  
  public void getAllProducts() {
    try {
      PreparedStatement statement = con.prepareStatement("SELECT name, url, brand FROM products_tbl");
      statement.execute();
      ResultSet rs = statement.getResultSet();
      while(rs.next()) {
        this.products.add(new Product(con, rs.getInt("id"), rs.getString("name"), rs.getString("url"), rs.getString("brand")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
