package com.davidpoza;

public class DirectorEmpleado implements Empleados {

  private CreacionInformes informeNuevo;
  public DirectorEmpleado(CreacionInformes informeNuevo) {
    this.informeNuevo = informeNuevo;
  }
  
  @Override
  public String getTareas() {
    // TODO Auto-generated method stub
    return "Gestionar la plantilla de la empresa";
  }

  @Override
  public String getInformes() {
    // TODO Auto-generated method stub
    return "Informe creado por el director:" + informeNuevo.getInforme();
  }

}
