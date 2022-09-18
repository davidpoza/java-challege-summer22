package com.davidpoza;

import org.springframework.beans.factory.annotation.Value;

public class DirectorFinanciero implements Empleados {
  private CreacionInformeFinanciero informeFinanciero;
  public DirectorFinanciero(CreacionInformeFinanciero informeFinanciero) {
    super();
    this.informeFinanciero = informeFinanciero;
  }
  
  @Override
  public String getTareas() {
    // TODO Auto-generated method stub
    return "Gestión y dirección de las operaciones financieras de la empresa";
  }

  @Override
  public String getInformes() {
    // TODO Auto-generated method stub
    return informeFinanciero.getInformeFinanciero();
  }

  @Value("${email}")
  private String email;
  
  public String getEmail() {
    return email;
  }

  
}
