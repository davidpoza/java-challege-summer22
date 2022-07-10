package com.davidpoza;

import java.io.File;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String currentWorkingDir = System.getProperty("user.dir");
      this.sendImageUploadingAFile(currentWorkingDir + "/tmp.png", update.getMessage().getChatId().toString());
    }
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
