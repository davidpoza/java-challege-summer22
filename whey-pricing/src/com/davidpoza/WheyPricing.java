package com.davidpoza;

import java.sql.Connection;
import java.sql.SQLException;

public class WheyPricing {

  public static void main(String[] args) throws Exception {
    final Connection con = DbConnection.connect();
      PriceChart chart = new PriceChart(con, null, null);
      chart.updatePrices();
      chart.draw();
    
    if (con != null) {
      try {
        con.close();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    }
  }

}
