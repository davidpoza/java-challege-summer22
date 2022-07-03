package com.davidpoza;


public class WheyPricing {

  public static void main(String[] args) throws Exception {
    MyProteinPrice p = new MyProteinPrice("nutricion-deportiva/impact-whey-protein/10530943.html?variation=12309348", 5.0);
    p.scrapPrice();
  }

}
