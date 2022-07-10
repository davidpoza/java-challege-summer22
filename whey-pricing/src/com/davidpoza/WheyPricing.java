package com.davidpoza;

import java.sql.Connection;
import java.sql.SQLException;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class WheyPricing {

  public static void main(String[] args) throws Exception {
    final Connection con = DbConnection.connect();
//      PriceChart chart = new PriceChart(con, null, null);
//      chart.updatePrices();
//      chart.draw();
    try {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new TelegramBot());
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
    
    if (con != null) {
      try {
        con.close();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    }
  }

}
