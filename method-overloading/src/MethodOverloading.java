
public class MethodOverloading {
  final static double CM_OF_INCH = 2.54;
  final static double INCHES_OF_FOOT = 12;
  
  public static void main(String[] args) {
    System.out.println(calcFeetAndInchesToCentimeters(5, 1));
    System.out.println(calcFeetAndInchesToCentimeters(1));
  }
  
  static double calcFeetAndInchesToCentimeters(double feet, double inches) {
    if(feet < 0) return -1;
    if(inches < 0 || inches > 12) return -1;
    return (feet*INCHES_OF_FOOT*CM_OF_INCH + inches*CM_OF_INCH);
  }
  
  static double calcFeetAndInchesToCentimeters(double inches) {
    return (calcFeetAndInchesToCentimeters(0, inches));
  }
}
