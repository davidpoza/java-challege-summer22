package com.davidpoza;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;

public class Price {
  protected String baseUrl;
  protected String productUrlPath;
  private Double price;
  private String brand;
  private Double discount;
  protected Double kg;
  
  public Price(String baseUrl, String productUrlPath, String brand, Double kg) {
    super();
    this.baseUrl = baseUrl;
    this.productUrlPath = productUrlPath;
    this.brand = brand;
    this.kg = kg;
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
  
  
}
