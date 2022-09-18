package com.davidpoza;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EjercicioAnotaciones {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext(EmpleadosConfig.class);
    DirectorFinanciero directorFinanciero = contexto.getBean("directorFinanciero", DirectorFinanciero.class);
    System.out.println(directorFinanciero.getInformes());
    System.out.println(directorFinanciero.getEmail());
    contexto.close();
  }

}
