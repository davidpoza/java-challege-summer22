package com.davidpoza;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UsoEmpleados {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    ClassPathXmlApplicationContext contexto = new ClassPathXmlApplicationContext("applicationContext.xml");
    SecretarioEmpleado Juan = contexto.getBean("miSecretario", SecretarioEmpleado.class);
    System.out.println(Juan.getTareas());
    System.out.println(Juan.getInformes());
    System.out.println(Juan.getEmail());
    contexto.close();
  }

}
