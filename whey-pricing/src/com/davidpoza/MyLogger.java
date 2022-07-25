package com.davidpoza;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MyLogger {
  public enum LogTypes {
    DEBUG, ERROR, FATAL, INFO, WARNING
  }

  private static Logger log = LogManager.getLogger(MyLogger.class);
  
  @SuppressWarnings("rawtypes")
  public static void log(Class clase, LogTypes tipo, String mensaje) {
    log = LogManager.getLogger(clase);
    
    switch (tipo) 
    {
      case DEBUG:
        log.debug(mensaje);
        break;
      case ERROR:
        log.error(mensaje);
        break;
      case FATAL:
        log.fatal(mensaje);
        break;
      case INFO:
        log.info(mensaje);
        break;
      case WARNING:
        log.warn(mensaje);
    }
  }
}