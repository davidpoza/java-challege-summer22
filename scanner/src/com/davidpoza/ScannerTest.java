package com.davidpoza;
import java.util.Scanner;

public class ScannerTest {

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    System.out.println("Introduce tu nombre");
    String name = scanner.nextLine();
    System.out.println("Hola " + name);
  }
  
}
