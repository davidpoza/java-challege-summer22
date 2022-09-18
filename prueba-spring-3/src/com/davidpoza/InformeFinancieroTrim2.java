package com.davidpoza;

import org.springframework.stereotype.Component;

@Component
public class InformeFinancieroTrim2 implements CreacionInformeFinanciero {

  @Override
  public String getInformeFinanciero() {
    // TODO Auto-generated method stub
    return "Presentación de informe financiero del trimestre 2.";
  }

}
