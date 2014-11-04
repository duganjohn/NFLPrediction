import java.io.*;
import java.util.*;

class LoadData {
  private String scoresCsv, scheduleCsv;
  private Game[] allGames, upcomingGames;
  private Team[] allTeams;
  private int allTeamsSize;
    
  public LoadData() {
    this(1, 1);
  }
  
  public LoadData(int scoresWeek, int scheduleWeek) {
    findNewestScoresAndSchedule(scoresWeek, scheduleWeek);
    this.allTeams = new Team[32];
    this.allGames = new Game[0];
    this.allTeamsSize = 0;
  }
  
  private void findNewestScoresAndSchedule(int scoresWeek, int scheduleWeek) {
    int num = 0;
    String inputName = "data/completedGames" + scoresWeek + "-" + num++ + ".csv";
    String next = "data/completedGames" + scoresWeek + "-" + num++ + ".csv";
    File f = new File(inputName);
    File f2 = new File(next);
    
    while (f2.exists() && !f2.isDirectory()) { 
      inputName = next;
      next = "data/completedGames" + scoresWeek + "-" + num++ + ".csv";
      f2 = new File(next);
    } 
    
    this.scoresCsv = inputName;
    System.out.println("Using " + inputName);
    
    ////
    
    num = 0;
    inputName = "data/schedule" + scheduleWeek + "-" + num++ + ".csv";
    next = "data/schedule" + scheduleWeek + "-" + num++ + ".csv";
    f = new File(inputName);
    f2 = new File(next);
    
    while (f2.exists() && !f2.isDirectory()) { 
      inputName = next;
      next = "data/schedule" + scheduleWeek + "-" + num++ + ".csv";
      f2 = new File(next);
    } 
    
    this.scheduleCsv = inputName;
    System.out.println("Using " + inputName);    
  }
  
  /*
   * Retrieves a team from allTeams
   *
   * @param the abbreviation of the team to retrieve
   * @return the team. If no team is found, it creates a new one.
   */
  private Team getTeam(String abbr) {
    Team team = new Team(abbr);
    for (int i = 0; i < allTeamsSize; i++)
      if (allTeams[i].equals(team)) return allTeams[i];
    
    allTeams[allTeamsSize++] = team;;
    
    return team;
  }
  
  /*
   * Creates upcomingGames
   *
   *
   */
  public void setup() {
    allGames = new Game[0];
    
    BufferedReader br = null;
    
    // Load previous games
    String line = "\n";
    try { 
      List<Game> games = new ArrayList<Game>();
      br = new BufferedReader(new FileReader(scoresCsv));
      while ((line = br.readLine()) != null) { 
        
        // Extract teams and score
        String[] game = line.split(","); 
        int week = Integer.parseInt(game[0]);       
        Team awayTeam = getTeam(game[1]);
        Team homeTeam = getTeam(game[2]);
        int awayScore = Integer.parseInt(game[3]);
        int homeScore = Integer.parseInt(game[4]);  
        Game thisGame = new Game(week, awayTeam, homeTeam, awayScore, homeScore);
        
        // Share the games with each Team
        awayTeam.addGame(thisGame);
        homeTeam.addGame(thisGame);
        
        games.add(thisGame);
      }

      allGames = (Game[])games.toArray(new Game[games.size()]); // Change the games arraylist into an array
 
    } 
    catch (Exception e) {
      e.printStackTrace();
    }  
     
    // Load schedule
    try { 
      List<Game> games = new ArrayList<Game>();
      br = new BufferedReader(new FileReader(scheduleCsv));
      while ((line = br.readLine()) != null) { 
        String[] game = line.split(",");
        
        Team awayTeam = getTeam(game[0]);
        Team homeTeam = getTeam(game[1]);

        Game thisGame = new Game(awayTeam, homeTeam);
        games.add(thisGame);
      }
      upcomingGames = (Game[])games.toArray(new Game[games.size()]); // Change the games arraylist into an array 
    } 
    catch (Exception e) {
      e.printStackTrace();
    }        
  }
    
  public Game[] getGames() {
    //if (allGames.length > 0) Arrays.sort(allGames);
    return allGames;
  }
  
  public Game[] getUpcomingGames() {
    //if (upcomingGames.length > 0) Arrays.sort(upcomingGames);
    return upcomingGames;
  }
  
  public Team[] getTeams() {
    if (allTeamsSize > 0) Arrays.sort(allTeams);
    return allTeams;
  }
  
  public static void main(String[] args) {
    LoadData load = new LoadData();
    load.setup();
    
    Arrays.sort(load.getTeams());
    for (Team team : load.getTeams()) System.out.println(team.toString());
  }

}