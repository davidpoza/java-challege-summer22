package com.davidpoza;

import java.sql.Connection;
import java.sql.SQLException;

public class WheyPricing {

  public static void main(String[] args) throws Exception {
    final Connection con = DbConnection.connect();
    MyProteinPrice p1 = new MyProteinPrice(con, "nutricion-deportiva/impact-whey-protein/10530943.html?variation=12309348", 5.0, 1);
    p1.scrapPrice();
    p1.save();
    
    HsnPrice p2 = new HsnPrice(con, "/marcas/raw-series/whey-protein-concentrate-80-2-0", 4.0, 1);
    p2.scrapPrice();
    p2.save();
    
    if (con != null) {
      try {
        con.close();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    }
  }

}
