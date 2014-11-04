import java.io.*;
import java.util.Scanner;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;

class ParseNFLPreviousGames {

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

  public void findGamesUpToWeek(int endWeek, String outputName) {
    for (int week = 1; week <= endWeek; week++) {
      String input = getHTML("http://www.nfl.com/scores/2014/REG" + week);
      
      int awayFind = input.indexOf("away-team");
      
      while (awayFind > 0) {
      
        // Get the away team
        int awayTeamStart = input.indexOf("team=", awayFind) + 5;
        int awayTeamEnd = input.indexOf("\"", awayTeamStart);
        String awayTeam = input.substring(awayTeamStart, awayTeamEnd);
        if (awayTeam.equals("JAX")) awayTeam = "JAC";

        // Get the home team
        int homeFind = input.indexOf("home-team", awayTeamEnd);
        int homeTeamStart = input.indexOf("team=", homeFind) + 5;
        int homeTeamEnd = input.indexOf("\"", homeTeamStart);
        String homeTeam = input.substring(homeTeamStart, homeTeamEnd);
        if (homeTeam.equals("JAX")) homeTeam = "JAC";

        // Get the away score
        int awayScoreStart = input.indexOf("total-score", awayTeamEnd) + 13;
        int awayScoreEnd = input.indexOf("<", awayScoreStart);
        int awayScore = 0;
        try {
          awayScore = Integer.parseInt(input.substring(awayScoreStart, awayScoreEnd));
        }
        catch(Exception e) {
          System.out.println("Broke");
          break;
        }

        // Get the home score
        int homeScoreStart = input.indexOf("total-score", homeTeamEnd) + 13;
        int homeScoreEnd = input.indexOf("<", homeScoreStart); 
        int homeScore = 0;
        try {
          homeScore = Integer.parseInt(input.substring(homeScoreStart, homeScoreEnd));
        }
        catch(Exception e) {
          System.out.println("Broke");
          break;
        }
            
        /*System.out.println("Week:\t" + week);
        System.out.println("Home Team is:\t" + homeTeam);
        System.out.println("Away Team is:\t" + awayTeam);
        System.out.println("Home Score:\t" + homeScore);
        System.out.println("Away Score:\t" + awayScore);
        System.out.println();*/  
        
        awayFind = input.indexOf("away-team", homeScoreEnd);  

        //Game game = new Game(team, opponent, pointsFor, pointsAgainst, homeGame);

        try {
          PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputName, true)));
          writer.append(week + ",");
          writer.append(awayTeam + ",");
          writer.append(homeTeam + ",");
          writer.append(awayScore + ",");
          writer.append(homeScore + "\n");
          writer.flush();
          writer.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  public String[] readInAbbrs(String abbrFileString) {
    BufferedReader br = null;
    String line = "";
    String[] teams = {};
    try {
      br = new BufferedReader(new FileReader(abbrFileString));
      while ((line = br.readLine()) != null) { 
        teams = line.split("\t");
      }
 
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }   
  
  return teams;
  }
  
  public static void main(String[] args) throws IOException {
    args = new String[] {"8"};
    int week = Integer.parseInt(args[0]);
    ParseNFLPreviousGames parser = new ParseNFLPreviousGames();
    Utilities util = new Utilities();
    util.createDataFolder();
    String outputName = util.createCSV("completedGames", week);
    System.out.println("Retrieving Past Games up to week " + week);

    parser.findGamesUpToWeek(week, outputName);
  }
}