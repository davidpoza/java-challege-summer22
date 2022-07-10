package com.davidpoza;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
    Document doc = null;
    Response res = Jsoup.connect(this.url)
      .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
      .ignoreContentType(true)
      .referrer("http://www.google.com")   
      .ignoreHttpErrors(true) 
      .execute();
    doc = Jsoup.connect(this.url)
      .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
      .ignoreContentType(true)
      .ignoreHttpErrors(true)
      .cookies(res.cookies())
      .get();
    Elements price = doc.select("div#ob-product-price");
    if (price.isEmpty()) throw new Exception("Price not found");
    String s = price.first().text();
    this.checkDiscount(doc);
    Double p = this.parsePrice(s);
    if (this.getDiscount() > 0) {
      p *= 1 - this.getDiscount()/100;
    }
    this.setAmount(p / this.getProduct().getKg());
    this.setDate(LocalDateTime.now());
    System.out.println("New price found for Prozis! " + this.getAmount());
  };
  
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
