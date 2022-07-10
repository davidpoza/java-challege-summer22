package com.davidpoza;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BulkPowdersPrice extends Price {
  
  public BulkPowdersPrice(Connection con) {
    super(con);
  }
  
  public BulkPowdersPrice(Connection con, String url, LocalDateTime date, Double amount, Product product) {
    super(con, url, date, amount, product);
  }
  
  public BulkPowdersPrice(Connection con, String url, Double amount, Product product) {
    super(con, url, amount, product);
  }
  
  @Override
  public void scrapPrice() throws Exception, IOException {   
    super.scrapPrice();
 // scraped prices in BUlkPowders always refer to 500g. we calculate the final price applying per-amount discount
    Double refKg = 0.5;
    Document doc = null;
    doc = Jsoup.connect(this.url).get();
    Elements price = doc.select("#price div.price-final_price span.price-wrapper");
    if (price.isEmpty()) throw new Exception("Price not found");
    String s = price.first().text();
//    this.checkDiscount(doc);
    if (this.getProduct().getKg() == 1.0) this.setDiscount(7.32d);
    else if(this.getProduct().getKg() == 2.5) this.setDiscount(16.73d);
    else if(this.getProduct().getKg() == 5.0) this.setDiscount(19.73d);
    Double p = this.parsePrice(s);
    if (this.getDiscount() > 0) {
      p *= 1 - this.getDiscount()/100;
    }
    this.setAmount(p / refKg);
    this.setDate(LocalDateTime.now());
    System.out.println("New price found for BulkPowders! " + this.getAmount());
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
