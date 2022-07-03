package com.davidpoza;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MyProteinPrice extends Price {
  public MyProteinPrice(String productUrlPath, Double kg) {
    super("https://www.myprotein.es/", productUrlPath, "MyProtein", kg);
  }
  
  @Override
  public void scrapPrice() throws Exception, IOException {   
    super.scrapPrice();
    Document doc = null;
    doc = Jsoup.connect(this.baseUrl + this.productUrlPath).get();
    Elements price = doc.select("p.productPrice_price");
    if (price.isEmpty()) throw new Exception("Price not found");
    String s = price.first().text();
    this.checkDiscount(doc);
    Double p = this.parsePrice(s);
    if (this.getDiscount() > 0) {
      p *= 1 - this.getDiscount()/100;
    }
    this.setPrice(p);
    System.out.println("Price found! " + this.getPrice());
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
