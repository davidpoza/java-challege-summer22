package com.davidpoza;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
    // scraped prices in Hsn always refer to 500g. we calculate the final price applying per-amount discount
    Double refKg = 0.5;
    Document doc = null;
    doc = Jsoup.connect(this.url).get();
    Elements price = doc.select("div.final-price");
    if (price.isEmpty()) throw new Exception("Price not found");
    String s = price.first().text();
//    this.checkDiscount(doc);
    if (this.getProduct().getKg() == 2d) this.setDiscount(20d);
    else if(this.getProduct().getKg() == 4d) this.setDiscount(25d);
    Double p = this.parsePrice(s);
    if (this.getDiscount() > 0) {
      p *= 1 - this.getDiscount()/100;
    }
    this.setAmount(p / refKg);
    this.setDate(LocalDateTime.now());
    System.out.println("Price found! " + this.getAmount());
  };
}
