package com.davidpoza;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MyProteinPrice extends Price {
  public MyProteinPrice(String productUrlPath) {
    super("https://www.myprotein.es/", productUrlPath, "MyProtein");
  }
  
  @Override
  public void scrapPrice() {   
    super.scrapPrice();
    Document doc = null;
    try {
      doc = Jsoup.connect(this.baseUrl + this.productUrlPath).get();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Elements precio = doc.select("p.productPrice_price");
    String s = precio.first().text();
    this.setPrice(this.parsePrice(s));
    System.out.println(this.getPrice());
  };
}
