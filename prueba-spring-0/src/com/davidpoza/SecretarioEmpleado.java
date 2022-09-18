package com.davidpoza;

public class SecretarioEmpleado implements Empleados {
  private CreacionInformes nuevoInforme;
  private String email;
  
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getTareas() {
    // TODO Auto-generated method stub
    return "Gestionar la agenda de los jefes";
  }

  @Override
  public String getInformes() {
    // TODO Auto-generated method stub
    return "Informe generado por el secretario "+ nuevoInforme.getInforme();
  }

  public void setNuevoInforme(CreacionInformes nuevoInforme) {
    this.nuevoInforme = nuevoInforme;
  }

}
