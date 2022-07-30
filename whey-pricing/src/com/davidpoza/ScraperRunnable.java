package com.davidpoza;

import java.sql.Connection;

import com.davidpoza.MyLogger.LogTypes;

public class ScraperRunnable implements Runnable {
  private PriceChart chart = null;
  private Connection con;
  
  public ScraperRunnable() {
    this.con = DbConnection.connect();
    this.chart = new PriceChart(this.con, null, null);    
  }
  
  @Override
  public void run() {
    try {      
      String[] commands = {"whey", "pea", "meat"};
      for (int i = 0; i<commands.length; i++) {
        chart.getAllProducts(commands[i]);
        chart.buildDataSet();
        chart.updatePrices();
        chart.draw(commands[i]);  
      }
    } catch (Exception ex) {
      MyLogger.log(TelegramBot.class, LogTypes.DEBUG, ex.getMessage());
    }   
  }

}
