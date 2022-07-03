package com.davidpoza;

public class HsnPrice extends Price {

  public HsnPrice(String baseUrl, String productUrlPath) {
    super("https://www.hsnstore.com/", productUrlPath, "HSN");
  }
  
}
