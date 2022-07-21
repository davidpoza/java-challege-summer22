package com.davidpoza;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class PriceChart {
  private Connection con;
  private ArrayList<Product> products = new ArrayList<Product>();
  private TimeSeriesCollection dataset = new TimeSeriesCollection();
  
  public  PriceChart(Connection con, LocalDateTime from, LocalDateTime to) {
    super();
    this.con = con;
    this.getAllProducts();
    this.buildDataSet();
  }
  
  
  public void getAllProducts() {
    try {
      PreparedStatement statement = con.prepareStatement("SELECT id, name, url, brand, kg FROM products_tbl");
      statement.execute();
      ResultSet rs = statement.getResultSet();
      while(rs.next()) {
        this.products.add(new Product(con, rs.getInt("id"), rs.getString("name"), rs.getString("url"), rs.getString("brand"), rs.getDouble("kg")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public void buildDataSet() {
    for (int i = 0; i < products.size(); i++) {
      Product p = products.get(i);
      TimeSeries series = new TimeSeries(p.getName());      
      for (int j = 0; j < p.getPrices().size(); j++) {
        Price price = p.getPrices().get(j);
        LocalDateTime date = price.getDate();
        series.add(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()), price.getAmount());
      }
      if(!series.isEmpty()) {
        this.dataset.addSeries(series);            
      }
    }
  }
  
  
  public void draw() {
    JFreeChart chart = ChartFactory.createTimeSeriesChart(
      "Whey protein price history",
      "Date", // X-Axis Label
      "Price per kg in €", // Y-Axis Label
      this.dataset
    );
    XYPlot plot = (XYPlot)chart.getPlot();
    plot.setBackgroundPaint(new Color(255,228,196));
    XYItemRenderer renderer = plot.getRenderer();
    final XYLineAndShapeRenderer rr = (XYLineAndShapeRenderer) renderer;
    XYItemLabelGenerator xy = new StandardXYItemLabelGenerator(
      StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT, 
      NumberFormat.getNumberInstance(),
      new DecimalFormat("#.##€")
    );
    DateAxis valueAxis = (DateAxis) plot.getDomainAxis();
    valueAxis.setVerticalTickLabels(true);
    // display dot and value
    rr.setBaseItemLabelGenerator( xy );
    rr.setBaseItemLabelsVisible(true);
    rr.setBaseLinesVisible(true);
    rr.setBaseItemLabelsVisible(true);  

    // custom colors
    rr.setSeriesPaint( 0, Color.BLUE );
    rr.setSeriesPaint( 1, Color.RED );
    rr.setSeriesPaint( 2, Color.ORANGE );
    rr.setSeriesPaint( 3, Color.GREEN);
    
    // custom line weight
    rr.setSeriesStroke(0, new BasicStroke(2.0f));
    rr.setSeriesStroke(1, new BasicStroke(2.0f));
    rr.setSeriesStroke(2, new BasicStroke(2.0f));
    rr.setSeriesStroke(3, new BasicStroke(2.0f));
    
    String dateFormat = "dd/MM/YY";
    DateAxis axis = (DateAxis) plot.getDomainAxis();
    axis.setDateFormatOverride(new SimpleDateFormat(dateFormat));
    axis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1));
    try {
      OutputStream out = new FileOutputStream("tmp.png");
      ChartUtilities.writeChartAsPNG(out,
        chart,
        800,
        600
      );
  
    } catch (IOException ex) {
        
    }
  }
  
  public void updatePrices( ) {
    for (int i = 0; i < products.size(); i++) {
      Product p = products.get(i);
      ArrayList<Price> prices = p.getPrices();
      Price last = prices.size() > 0 ? prices.get(prices.size() - 1) : null;
      LocalDateTime today = LocalDateTime.now();
      if (prices.size() == 0 
          || last.getDate().getDayOfMonth() != today.getDayOfMonth()
          || last.getDate().getMonthValue() != today.getMonthValue()
          || last.getDate().getYear() != today.getYear()    
         ){
        Price newPrice = new PriceFactory(con, p.getBrand()).getPrice();
        newPrice.setUrl(p.getUrl());
        newPrice.setProduct(p);
        try {
          newPrice.scrapPrice();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
        }
        newPrice.save();        
      }
    }  
  }
  
  public void sendToTelegram() {
    


  }
}
