package com.davidpoza;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
import java.util.Collections;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.TextAnchor;

import com.davidpoza.MyLogger.LogTypes;
import com.davidpoza.PriceFactory.Brand;

public class PriceChart {
  private Connection con;
  private ArrayList<Product> products = new ArrayList<Product>();
  private ArrayList<XYDataset> datasets = new ArrayList<XYDataset>();
  
  public  PriceChart(Connection con, LocalDateTime from, LocalDateTime to) {
    super();
    this.con = con;
  }
  
  private ArrayList<Integer> getProductIdsByCommand(String command) {
    ArrayList<Integer> ret = new ArrayList<>();
    switch(command) {
    case "whey":
      Collections.addAll(ret, new Integer[] {1, 2, 3, 4});
      break;
    case "meat":
      Collections.addAll(ret, new Integer[] {7,8,9});
      break;
    case "rice":
      Collections.addAll(ret, new Integer[] {5,6,10});
      break;
    case "pea":
      Collections.addAll(ret, new Integer[] {11,12,13});
      break;
    default:
      break;
    }
    return ret;
  }
  
  public void getAllProducts(String command) {
    this.products.clear();
    PreparedStatement statement = null;
    ArrayList<Integer> productIds = this.getProductIdsByCommand(command);
    String productIdsStr = "";
    for (int i=0; i<productIds.size(); i++) {
      productIdsStr += String.valueOf(productIds.get(i));
      if (i<(productIds.size()-1)) productIdsStr += ",";
    }
    try {
      MyLogger.log(PriceChart.class, LogTypes.DEBUG, "querying all products");
      statement = con.prepareStatement("SELECT id, name, url, brand, kg FROM products_tbl WHERE id IN ("+ productIdsStr +")");
//      Array array = con.createArrayOf("INTEGER", productIds.toArray());
//      statement.setArray(1, array);
      statement.execute();
      MyLogger.log(PriceChart.class, LogTypes.DEBUG, statement.toString());
      ResultSet rs = statement.getResultSet();
      while(rs.next()) {
        MyLogger.log(PriceChart.class, LogTypes.DEBUG, "fetched product:" + rs.getString("name"));
        this.products.add(new Product(con, rs.getInt("id"), rs.getString("name"), rs.getString("url"), rs.getString("brand"), rs.getDouble("kg")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * We don't repeat consecutive equal consecutive values
   */
  public void buildDataSet() {
    this.datasets.clear();
    try {
      MyLogger.log(PriceChart.class, LogTypes.DEBUG, "building dataset");
      for (int i = 0; i < products.size(); i++) {
        Product p = products.get(i);
        TimeSeriesCollection dataset = new TimeSeriesCollection();  
        TimeSeries series = new TimeSeries(p.getName() + " (" + p.getBrand() +  ")");    
        MyLogger.log(PriceChart.class, LogTypes.DEBUG, "creating timeseries for " + p.getName());
        for (int j = 0; j < p.getPrices().size(); j++) {
          Price price = p.getPrices().get(j);
          LocalDateTime date = price.getDate();
          if (j == 0 || j == p.getPrices().size()-1 || j > 0 && price.getAmount().doubleValue() != p.getPrices().get(j - 1).getAmount().doubleValue()) {  
            // first and last one always are drawn
            series.add(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()), price.getAmount());
          }
        }
        MyLogger.log(PriceChart.class, LogTypes.DEBUG, "adding series to dataset");
        dataset.addSeries(series);
        this.datasets.add(dataset);
      }
    } catch (Exception e) {
      MyLogger.log(PriceChart.class, LogTypes.DEBUG, e.getMessage());
    }
  }
  
  public Color getColor(String brand) {
    switch(Brand.valueOf(brand)) {
    case MYPROTEIN:
      return new Color(0, 129, 144);
    case HSN:
      return new Color(255, 96, 0);
    case PROZIS:
      return new Color(190, 41, 27);
    case BULKPOWDERS:
      return new Color(135, 224, 0);
    default:
      return Color.BLACK;
    }
  }
  
  public void draw(String filename) {   
    MyLogger.log(PriceChart.class, LogTypes.DEBUG, "drawing charts");
    DateAxis domainAxis = new DateAxis("Date");
    String dateFormat = "dd MMM";
    domainAxis.setDateFormatOverride(new SimpleDateFormat(dateFormat));
    domainAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1));
    domainAxis.setVerticalTickLabels(true);
    CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domainAxis);
    
    for (int i=0; i < this.datasets.size(); i++) {
      MyLogger.log(PriceChart.class, LogTypes.DEBUG, "drawing chart:" + this.products.get(i).getBrand());
      XYDataset dataset = this.datasets.get(i);
      NumberAxis rangeAxis = new NumberAxis("Price per kg in €");
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      
      XYLineAndShapeRenderer renderer = new  XYLineAndShapeRenderer();
      XYItemLabelGenerator xy = new StandardXYItemLabelGenerator(
        StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT, 
        NumberFormat.getNumberInstance(),
        new DecimalFormat("#.##€")
      );
      renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
          ItemLabelAnchor.OUTSIDE8, TextAnchor.TOP_CENTER));
      renderer.setBaseItemLabelGenerator(xy);
      renderer.setBaseItemLabelsVisible(true);
      renderer.setBaseLinesVisible(true);
      renderer.setBaseItemLabelsVisible(true); 
      renderer.setSeriesPaint( 0, this.getColor(this.products.get(i).getBrand()));
      renderer.setSeriesStroke(0, new BasicStroke(2.0f));
      
      XYPlot subplot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
      subplot.setDomainGridlinesVisible(true);    
      plot.add(subplot);
    }
    
    JFreeChart chart = new JFreeChart(
      filename.substring(0, 1).toUpperCase() + filename.substring(1) + " price history",
      new Font("SansSerif", Font.BOLD, 12),
      plot,
      true
    );
    
    try {
      MyLogger.log(PriceChart.class, LogTypes.DEBUG, "saving chart as image");
      OutputStream out = new FileOutputStream(filename + ".png");
      ChartUtilities.writeChartAsPNG(out,
        chart,
        800,
        600
      );
      out.close();
    } catch (IOException e) {
      MyLogger.log(PriceChart.class, LogTypes.DEBUG, e.getMessage());
    }
  }
  
  public void updatePrices( ) {
    MyLogger.log(PriceChart.class, LogTypes.DEBUG, "updating prices");
    for (int i = 0; i < products.size(); i++) {
      Product p = products.get(i);
      p.readPrices();
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
  
}
