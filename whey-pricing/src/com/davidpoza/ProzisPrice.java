package com.davidpoza;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ProzisPrice extends Price {
  
  public ProzisPrice(Connection con) {
    super(con);
  }
  
  public ProzisPrice(Connection con, String url, LocalDateTime date, Double amount, Product product) {
    super(con, url, date, amount, product);
  }
  
  public ProzisPrice(Connection con, String url, Double amount, Product product) {
    super(con, url, amount, product);
  }
  
  @Override
  public void scrapPrice() throws Exception, IOException {   
    super.scrapPrice();
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet request = new HttpGet("https://files.davidinformatico.com/whey-scraper/prozis.json");
    CloseableHttpResponse response = httpClient.execute(request);
    int statusCode = response.getStatusLine().getStatusCode();
    if (statusCode != 200) 
    {
      throw new Exception("Price not found");
    }
    String jsonString = EntityUtils.toString(response.getEntity());
    JSONObject obj = new JSONObject(jsonString);
    String price = obj.getString("price");
    long ts = obj.getLong("date");
    
//    this.checkDiscount(doc);
    if (this.getProduct().getKg() == 2d) this.setDiscount(20d);
    else if(this.getProduct().getKg() == 4d) this.setDiscount(25d);
    Double p = this.parsePrice(price);
    if (this.getDiscount() > 0) {
      p *= 1 - this.getDiscount()/100;
    }
    this.setAmount(p / this.getProduct().getKg());
    this.setDate(new Timestamp(ts).toLocalDateTime());
    System.out.println("New price found for Prozis! " + this.getAmount());
  }
  
  @Override
  protected void checkDiscount(Document doc) {
    Elements discount = doc.select("span.coupon-title");
    if (discount.isEmpty()) return;
    String s = discount.first().text();
    Pattern pattern = Pattern.compile("\\d*%");
    Matcher matcher = pattern.matcher(s);
    int i = 0;
    while (matcher.find()) {
      // it's possible to have two discounts, the first is already applied
      i = Integer.parseInt(matcher.group(0).replaceFirst("%", ""));
      System.out.println("Has discount! " + i + "%");
    }
    if (i > 0) this.setDiscount(Double.valueOf(i));
  }

}
