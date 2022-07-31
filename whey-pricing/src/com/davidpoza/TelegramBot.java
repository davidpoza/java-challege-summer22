package com.davidpoza;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.SQLException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.davidpoza.MyLogger.LogTypes;

import io.github.cdimascio.dotenv.Dotenv;

public class TelegramBot extends TelegramLongPollingBot {
  @Override
  public String getBotToken() {
    Dotenv dotenv = null;
    dotenv = Dotenv.configure()
      .ignoreIfMalformed()
      .ignoreIfMissing()
      .load();
    String apiKey = dotenv.get("API_KEY_TELEGRAM");
    return apiKey;
  }
  
  private void processCommand(String command, String chatId) {
    MyLogger.log(TelegramBot.class, LogTypes.DEBUG, "Received message");
    final Connection con = DbConnection.connect();
    boolean doesExist = true;
    File file = null;
    
    try {
      file = new File(command + ".png");
      doesExist = file.exists();
    }
    catch (Exception e) {
      doesExist = false;
    }
    
    try {
      FileTime creationTime = doesExist
        ? (FileTime) Files.getAttribute(file.toPath(), "lastModifiedTime")
        : null;
      long imageAgeMs = doesExist 
        ? System.currentTimeMillis() - creationTime.toMillis()
        : 0;
      MyLogger.log(TelegramBot.class, LogTypes.DEBUG, "Image is " + String.valueOf(imageAgeMs/1000) + " seconds old");
    } catch (Exception ex) {
      MyLogger.log(TelegramBot.class, LogTypes.DEBUG, ex.getMessage());
    }      
    this.sendImageUploadingAFile(file.getAbsolutePath(), chatId);
    if (con != null) {
      try {
        con.close();
        MyLogger.log(TelegramBot.class, LogTypes.DEBUG, "Connection to SQLite has been closed");
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    }
  }

  @Override
  public void onUpdateReceived(Update update) {
    Message message = update.getMessage();
    String input = message.getText();
    this.processCommand(input.substring(1), update.getMessage().getChatId().toString());
  }

  @Override
  public String getBotUsername() {
      return "WheyPricesESBot";
  }
  
  public void sendImageUploadingAFile(String filePath, String chatId) {
    SendPhoto sendPhotoRequest = new SendPhoto();
    sendPhotoRequest.setChatId(chatId);
    sendPhotoRequest.setPhoto(new InputFile(new File(filePath)));
    try {
      execute(sendPhotoRequest);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
}

}
