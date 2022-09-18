package com.davidpoza;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EjercicioAnotaciones {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    ClassPathXmlApplicationContext contexto = new ClassPathXmlApplicationContext("applicationContext.xml");
    Empleados Juan = contexto.getBean("comercialExperimentado", Empleados.class);
    System.out.println(Juan.getTareas());
    System.out.println(Juan.getInformes());
    contexto.close();
  }

}
