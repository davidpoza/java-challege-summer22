package com.davidpoza;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HsnPrice extends Price {
  
  HsnPrice(Connection con) {
    super(con);
  }

  public HsnPrice(Connection con, String url, LocalDateTime date, Double amount, Product product) {
    super(con, url, date, amount, product);
  }
  
  public HsnPrice(Connection con, String url, Double amount, Product product) {
    super(con, url, amount, product);
  }
  
  @Override
  public void scrapPrice() throws Exception, IOException {   
    super.scrapPrice();
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet request = new HttpGet("https://files.davidinformatico.com/whey-scraper/hsn.json");
    CloseableHttpResponse response = httpClient.execute(request);
    int statusCode = response.getStatusLine().getStatusCode();
    if (statusCode != 200) {
      throw new Exception("Price not found");
    }
    String jsonString = EntityUtils.toString(response.getEntity());
    JSONObject obj = new JSONObject(jsonString);
    JSONObject prices = obj.getJSONObject("prices");
    try {
      String price = prices.getString(this.getUrl());
      Double p = this.parsePrice(price);
      this.setAmount(p / this.getProduct().getKg());
    } catch (Exception ex) {
      throw new Exception("Price not found");
    }
    this.setDate(LocalDateTime.now());
    System.out.println("New price found for HSN! " + this.getAmount());
  };
}
