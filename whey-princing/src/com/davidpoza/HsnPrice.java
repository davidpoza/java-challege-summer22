package com.davidpoza;

public class HsnPrice extends Price {

  public HsnPrice(String productUrlPath, Double kg) {
    super("https://www.hsnstore.com/", productUrlPath, "HSN", kg);
  }
  
}
