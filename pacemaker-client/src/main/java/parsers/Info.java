package parsers;

//import static org.fusesource.jansi.Ansi.Color.*;




public class Info
{

  // Relevant Colors to be used in System
  
  public static final String ANSI_RESET = "\u001B[0m";
  

//All Bold fr better show
  public static final String BLACK = "\033[1;30m";  // BLACK
  public static final String RED = "\033[1;31m";    // RED
  public static final String GREEN = "\033[1;32m";  // GREEN
  public static final String YELLOW = "\033[1;33m"; // YELLOW
  public static final String BLUE = "\033[1;34m";   // BLUE
  public static final String PURPLE = "\033[1;35m"; // PURPLE
  
    
  //error
  public static void err(String optionName, String message)
  {
   System.out.println(RED + optionName  + " - " + message + ANSI_RESET);
  }

  //warnings
  public static void warn(String optionName, String message)
  {
   System.out.println(PURPLE + optionName  + " - " + message + ANSI_RESET);
  }

    
  //info
  public static void info( String message)
  {
   System.out.println(BLUE +  message + ANSI_RESET);
  }
  
  
}
