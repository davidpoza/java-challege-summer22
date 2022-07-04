package com.davidpoza;


public class WheyPricing {

  public static void main(String[] args) throws Exception {
    Connect.connect();
    
    MyProteinPrice p1 = new MyProteinPrice("nutricion-deportiva/impact-whey-protein/10530943.html?variation=12309348", 5.0);
    p1.scrapPrice();
    
    HsnPrice p2 = new HsnPrice("/marcas/raw-series/whey-protein-concentrate-80-2-0", 4.0);
    p2.scrapPrice();
  }

}
