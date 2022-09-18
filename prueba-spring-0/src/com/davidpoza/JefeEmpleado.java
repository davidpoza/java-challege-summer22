package com.davidpoza;

public class JefeEmpleado implements Empleados {
  private CreacionInformes informeNuevo;
  public JefeEmpleado(CreacionInformes informeNuevo) {
    this.informeNuevo = informeNuevo;
  }
  
  public String getTareas() {
    return "Gestiono las cuestiones relativas a mis empleados de sección";
  }

  @Override
  public String getInformes() {
    // TODO Auto-generated method stub
    return "Informe creado por el jefe:" + informeNuevo.getInforme();
  }
}
