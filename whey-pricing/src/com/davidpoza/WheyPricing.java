package com.davidpoza;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.davidpoza.MyLogger.LogTypes;

public class WheyPricing {

  public static void main(String[] args) throws Exception {
    MyLogger.log(WheyPricing.class, LogTypes.DEBUG, "starting...");
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(new ScraperRunnable(), 0, 8, TimeUnit.HOURS);
    try {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new TelegramBot());
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
    
    
  }

}
