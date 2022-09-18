package com.davidpoza;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.davidpoza")
@PropertySource("classpath:datos.properties")
public class EmpleadosConfig {
 
  // el nombre del método es el nombre del bean
  @Bean
  public CreacionInformeFinanciero informeFinancieroDtoCompras() {
    return new InformeFinancieroDtoCompras();
  }
  
  
  @Bean
  public Empleados directorFinanciero() {
    return new DirectorFinanciero(informeFinancieroDtoCompras());
  }
}
