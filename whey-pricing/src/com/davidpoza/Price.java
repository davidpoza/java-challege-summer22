package com.davidpoza;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

public class Price {
  protected String baseUrl;
  protected String productUrlPath;
  private LocalDateTime date;
  private Double price;
  private String brand;
  private int productId;
  private Double discount;
  protected Double kg;
  protected Connection con;
  
  public Price(Connection con, String baseUrl, String productUrlPath, LocalDateTime date, String brand, Double kg, int productId) {
    super();
    this.baseUrl = baseUrl;
    this.productUrlPath = productUrlPath;
    this.brand = brand;
    this.kg = kg;
    this.discount = 0.0;
    this.date = date;
    this.productId = productId;
    this.con = con;
  }
  
  public Price(Connection con, String baseUrl, String productUrlPath, String brand, Double kg, int productId) {
    this(con, baseUrl, productUrlPath, LocalDateTime.now(), brand, kg, productId);
  }

  protected Double parsePrice(String price) throws Exception {
    Pattern pattern = Pattern.compile("\\d*,?\\d*");
    Matcher matcher = pattern.matcher(price);
    if (matcher.find()) {
      Double d = Double.parseDouble(matcher.group(0).replace(',', '.'));
      return d;
    }
    throw new Exception("Not able to parse price");
  }
  
  public void scrapPrice() throws Exception, IOException {   
    System.out.println("Fetching...");
  }
  
  protected void checkDiscount(Document doc) {     }

  public void save() {
    try {
      PreparedStatement statement = con.prepareStatement("INSERT INTO prices_tbl (date, amount, discount, product_id) VALUES (?, ?, ?, ?)");
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
      statement.setString(1, this.date.format(dateTimeFormatter));
      statement.setDouble(2, this.price);
      statement.setDouble(3, this.discount);
      statement.setInt(4, this.productId);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  private void validate() {
    
  }
  
  private boolean exists() {
    return false;
  }
  
  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }

  public String getProductUrlPath() {
    return productUrlPath;
  }

  public void setProductUrlPath(String productUrlPath) {
    this.productUrlPath = productUrlPath;
  }

  public Double getKg() {
    return kg;
  }

  public void setKg(Double kg) {
    this.kg = kg;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }
  
  
}
