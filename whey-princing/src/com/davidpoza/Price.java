package com.davidpoza;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Price {
  protected String baseUrl;
  protected String productUrlPath;
  private Double price;
  private String brand;
  private Double discount;
  
  public Price(String baseUrl, String productUrlPath, String brand) {
    super();
    this.baseUrl = baseUrl;
    this.productUrlPath = productUrlPath;
    this.brand = brand;
  }

  protected Double parsePrice(String price) {
    Pattern pattern = Pattern.compile("\\d*,?\\d*");
    Matcher matcher = pattern.matcher(price);
    if (matcher.find()) {
      Double d = Double.parseDouble(matcher.group(0).replace(',', '.'));
      return d;
    }
    return -1d;
  }
  
  public void scrapPrice() {   
    System.out.println("Fetching...");
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
  
  
}
