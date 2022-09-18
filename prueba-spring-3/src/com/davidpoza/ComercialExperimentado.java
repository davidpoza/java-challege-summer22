package com.davidpoza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ComercialExperimentado implements Empleados {
  
  /**
   * Con el autowired se busca en todo el proyecto en busca de una clase
   * que implemente la interfaz CreacionInformeFinanciero
   */
  
  @Autowired
  
  public ComercialExperimentado(@Qualifier("informeFinancieroTrim2") CreacionInformeFinanciero nuevoInforme) {
    this.nuevoInforme = nuevoInforme;
  }

  private CreacionInformeFinanciero nuevoInforme;
  
  @Override
  public String getTareas() {
    // TODO Auto-generated method stub
    return "Vender, vender y vender más";
  }

  @Override
  public String getInformes() {
    // TODO Auto-generated method stub
    return nuevoInforme.getInformeFinanciero();
  }

}
