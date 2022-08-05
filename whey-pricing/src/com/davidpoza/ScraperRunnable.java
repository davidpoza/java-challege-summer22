package com.davidpoza;

import java.sql.Connection;

import com.davidpoza.MyLogger.LogTypes;

public class ScraperRunnable implements Runnable {
  private Connection con;
  
  public ScraperRunnable() {
    this.con = DbConnection.connect();       
  }
  
  @Override
  public void run() {
    try {      
      String[] commands = {"whey", "pea", "meat", "rice", "other"};
      for (int i = 0; i<commands.length; i++) {
        PriceChart chart = new PriceChart(this.con, null, null); 
        MyLogger.log(ScraperRunnable.class, LogTypes.DEBUG, "refreshing data for " + commands[i] + "chart");
        chart.getAllProducts(commands[i]);
        chart.buildDataSet();
        chart.updatePrices();
        chart.draw(commands[i]);  
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      MyLogger.log(ScraperRunnable.class, LogTypes.DEBUG, ex.getMessage() + ex.getStackTrace());
    }   
  }

}
