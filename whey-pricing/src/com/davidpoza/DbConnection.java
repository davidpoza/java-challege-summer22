package com.davidpoza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.davidpoza.MyLogger.LogTypes;

public class DbConnection {
  public static Connection connect() {
    Connection conn = null;
    try {
      String url = "jdbc:sqlite:test.db";
      conn = DriverManager.getConnection(url);
      MyLogger.log(WheyPricing.class, LogTypes.DEBUG, "Connection to SQLite has been established");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } 
    return conn;
  }
}