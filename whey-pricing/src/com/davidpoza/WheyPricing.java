package com.davidpoza;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.davidpoza.MyLogger.LogTypes;

public class WheyPricing {

  public static void main(String[] args) throws Exception {
    MyLogger.log(WheyPricing.class, LogTypes.DEBUG, "starting...");
    try {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new TelegramBot());
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
    
    
  }

}
