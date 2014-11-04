import java.io.*;
import java.util.Scanner;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;
import java.util.*;

class ParseNFLSchedule {
  List<String> teams = new ArrayList<String>();  
  
  public String getHTML(String urlString) {   
    String HTML = "";
    try {
      URL url = new URL(urlString);
      Scanner s = new Scanner(url.openStream());
      
      while (s.hasNext()) {
        HTML += s.next();     
      }
    }
    catch(IOException ex) {
       ex.printStackTrace();
    }
    
    return HTML;
  }
  
  public void findGames(String input, String outputName) {
    int formattedDate = input.indexOf("formattedDate");
    String away = "";
    
    while (formattedDate > 0) {
      int awayTeam = input.indexOf("awayAbbr", formattedDate) + 9;
      int awayTeamEnd = input.indexOf("-", awayTeam);
      if (away.equals(input.substring(awayTeam, awayTeamEnd))) {  // Duplicate game
        formattedDate = awayTeamEnd;
        continue;
      }
      away = input.substring(awayTeam, awayTeamEnd);
      
      int homeTeam = input.indexOf("homeAbbr", awayTeamEnd) + 9;
      int homeTeamEnd = input.indexOf("-", homeTeam);
      String home = input.substring(homeTeam, homeTeamEnd);
      
      try {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputName, true)));
        writer.append(away + ",");
        writer.append(home + "\n");
        writer.flush();
        writer.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }  
      formattedDate = input.indexOf("formattedDate", homeTeamEnd);
    }    
  }
  
  public static void main(String[] args) throws IOException {
    ParseNFLSchedule parser = new ParseNFLSchedule();
    Utilities util = new Utilities();
    int week = Integer.parseInt(args[0]);
    
    util.createDataFolder();    
    String outputName = util.createCSV("schedule", week);
    System.out.println("Creating schedule for week " + week);

    String url = "http://www.nfl.com/schedules/2014/REG" + week;
    String sourceText = parser.getHTML(url);
    parser.findGames(sourceText, outputName);
  }
}