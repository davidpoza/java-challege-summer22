package com.davidpoza;

import java.sql.Connection;
import java.time.LocalDateTime;


public class PriceFactory {
  private Price price;
  public enum Brand {
    MYPROTEIN,
    HSN,
    PROZIS,
    BULKPOWDERS
  }

  PriceFactory(Connection con, String brand, String url, LocalDateTime date, Double amount, Product product) {
    this.price = null;
    switch(Brand.valueOf(brand)) {
      case MYPROTEIN:
        this.price = new MyProteinPrice(con, url, date, amount, product);
        break;
      case HSN:
        this.price = new HsnPrice(con, url, date, amount, product);
        break;
      case PROZIS:
        this.price = new ProzisPrice(con, url, date, amount, product);
        break;
      case BULKPOWDERS:
        break;
    };
  }
  
  PriceFactory(Connection con, String brand) {
    this.price = null;
    switch(Brand.valueOf(brand)) {
      case MYPROTEIN:
        this.price = new MyProteinPrice(con);
        break;
      case HSN:
        this.price = new HsnPrice(con);
        break;
      case PROZIS:
        this.price = new ProzisPrice(con);
        break;
      case BULKPOWDERS:
        break;
    };
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price p) {
    this.price = p;
  }
  
  
}
