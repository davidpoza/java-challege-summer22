package com.davidpoza;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HsnPrice extends Price {

  public HsnPrice(Connection con, String productUrlPath, LocalDateTime date, Double kg, int productId) {
    super(con, "https://www.hsnstore.com/", productUrlPath, date, "HSN", kg, productId);
  }
  
  public HsnPrice(Connection con, String productUrlPath, Double kg, int productId) {
    super(con, "https://www.hsnstore.com/", productUrlPath, "HSN", kg, productId);
  }
  
  @Override
  public void scrapPrice() throws Exception, IOException {   
    super.scrapPrice();
    // scraped prices in Hsn always refer to 500g. we calculate the final price applying per-amount discount
    Double refKg = 0.5;
    Document doc = null;
    doc = Jsoup.connect(this.baseUrl + this.productUrlPath).get();
    Elements price = doc.select("div.final-price");
    if (price.isEmpty()) throw new Exception("Price not found");
    String s = price.first().text();
//    this.checkDiscount(doc);
    if (this.getKg() == 2d) this.setDiscount(20d);
    else if(this.getKg() == 4d)this.setDiscount(25d);
    Double p = this.parsePrice(s);
    if (this.getDiscount() > 0) {
      p *= 1 - this.getDiscount()/100;
    }
    this.setPrice(p / refKg);
    System.out.println("Price found! " + this.getPrice());
  };
}
