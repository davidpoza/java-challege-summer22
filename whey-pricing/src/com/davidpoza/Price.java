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
  protected String url;
  private LocalDateTime date;
  private Double amount;
  private Product product;
  private Double discount;
  protected Double kg;
  protected Connection con;
  
  public Price(Connection con) {
    this.url = null;
    this.kg = null;
    this.discount = 0d;
    this.date = null;
    this.product = null;
    this.con = con;
    this.amount = null;
  }
  
  public Price(Connection con, String url, LocalDateTime date, Double amount, Product product) {
    super();
    this.url = url;
    this.discount = 0.0;
    this.date = date;
    this.product = product;
    this.con = con;
    this.amount = amount;
  }
  
  public Price(Connection con, String url, Double amount, Product product) {
    this(con, url, LocalDateTime.now(), amount, product);
  }

  protected Double parsePrice(String price) throws Exception {
    Pattern pattern = Pattern.compile("\\d*[,.]?\\d*");
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
      if (!this.isValid()) return;
      PreparedStatement statement = con.prepareStatement("INSERT INTO prices_tbl (date, amount, discount, product_id) VALUES (?, ?, ?, ?)");
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
      statement.setString(1, this.date.format(dateTimeFormatter));
      statement.setDouble(2, this.amount);
      statement.setDouble(3, this.discount);
      statement.setInt(4, this.product.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  private boolean isValid( ) {
    if (this.amount == null
        || this.product == null 
        || this.date == null) {
      return false;
    }
    return true;
  }
  
  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }


  
}
