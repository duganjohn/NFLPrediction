import java.io.*;

class Utilities {
 
  public void createDataFolder() {    
    File dataFolder = new File("data");

    if (!dataFolder.exists()) {
      boolean result = false;

      try{
          dataFolder.mkdir();
          result = true;
       } catch(SecurityException se){
          //handle it
       }        
       if(result) {    
         System.out.println("Data directory created");  
       }
    }
  }

  public String createCSV(String prefix, int week) {
    int num = 0;
    String outputName = "data/" + prefix + week + "-" + num++ + ".csv";
    File f = new File(outputName);
    while (f.exists() && !f.isDirectory()) { 
      outputName = "data/" + prefix + week + "-" + num++ + ".csv";
      f = new File(outputName);
    }    

    System.out.println("Making " + outputName);

    try {
      f.createNewFile();
    }
    catch (IOException e) {
      System.out.println("Did not make file " + e);
    }
    
    return outputName;
  }
}