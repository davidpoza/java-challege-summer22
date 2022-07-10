package com.davidpoza;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MyProteinPrice extends Price {
  
  public MyProteinPrice(Connection con) {
    super(con);
  }
  
  public MyProteinPrice(Connection con, String url, LocalDateTime date, Double amount, Product product) {
    super(con, url, date, amount, product);
  }
  
  public MyProteinPrice(Connection con, String url, Double amount, Product product) {
    super(con, url, amount, product);
  }
  
  @Override
  public void scrapPrice() throws Exception, IOException {   
    super.scrapPrice();
    Document doc = null;
    doc = Jsoup.connect(this.url).get();
    Elements price = doc.select("p.productPrice_price");
    if (price.isEmpty()) throw new Exception("Price not found");
    String s = price.first().text();
    this.checkDiscount(doc);
    Double p = this.parsePrice(s);
    if (this.getDiscount() > 0) {
      p *= 1 - this.getDiscount()/100;
    }
    this.setAmount(p / this.getProduct().getKg());
    this.setDate(LocalDateTime.now());
    System.out.println("Price found! " + this.getAmount());
  };
  
  @Override
  protected void checkDiscount(Document doc) {
    Elements discount = doc.select("span.papBanner_text");
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
