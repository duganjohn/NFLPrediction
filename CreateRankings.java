import java.io.*;
import java.util.Scanner;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;

class CreateRankings {
  Team[] allTeams;
  Game[] allGames, upcomingGames;
  String[] week8Results;
  boolean headerWritten;
  
  public CreateRankings(int dataWeek, int scheduleWeek) {
    LoadData data = new LoadData(dataWeek, scheduleWeek);
    data.setup();
    allTeams = data.getTeams();
    allGames = data.getGames();
    upcomingGames = data.getUpcomingGames(); 
    week8Results = new String[] {"DEN","DET","CIN","NE","MIA","MIN","CLE","NO","BUF","SEA","HOU","KC","ARI","PIT","WAS"};
    headerWritten = false;
  }
  
  private void printRankings() {
    System.out.println("Rank\tTeam\tW-L\tPFpg\tPApg");
    System.out.println("____\t____\t____\t____\t____");
    for (int i = 0; i < allTeams.length; i++) {
      Team team = allTeams[i];
      System.out.println((i + 1) + ".\t" + team.toString() + "\t" + team.getWins() 
      + "-" + team.getLosses() + "\t" + team.getPointsForPG() + "\t" + team.getPointsAgainstPG());
    }
  }

  private void writePredictionHeader(Game[] games, String outputName) {
    for (Game game : games) {
      try {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputName, true)));
        writer.append(game.getAwayTeam() + " @ " + game.getHomeTeam());
        writer.append(",");
        writer.flush();
        writer.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }  
    }    
            
    try {
      PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputName, true)));
      writer.append("\n");
      writer.flush();
      writer.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void predictByVar(String var, String outputName) {
    int numCorrect = 0;
    Game[] games = upcomingGames;//

    if (!headerWritten) {
      writePredictionHeader(games, outputName);
      headerWritten = true;
    }
        
    int totalGames = games.length;
    for (int i = 0; i < totalGames; i++) {
      Game game = games[i];

      Team home = game.getHomeTeam();
      Team away = game.getAwayTeam();
      double homeVar, awayVar;
      
      if (var.equals("rank")) {
        homeVar = getRank(away);
        awayVar = getRank(home);
      }
      
      else if (var.equals("pointsFor")) {
        homeVar = home.getPointsForPG();
        awayVar = away.getPointsForPG();
      }
      
      else if (var.equals("pointsAgainst")) {
        homeVar = away.getPointsAgainstPG();
        awayVar = home.getPointsAgainstPG();
      }
      
      else if (var.equals("winPercent")) {
        homeVar = home.getWinPercent();
        awayVar = away.getWinPercent();
      }
      
      else if(var.equals("pointDiff")) {
        homeVar = home.getPointDiffPG();
        awayVar = away.getPointDiffPG();
      }
      
      else if(var.equals("powerPoints")) {
        homeVar = getPowerPoints(home);
        awayVar = getPowerPoints(away);
      }
      
      else if(var.equals("powerPoints2")) {
        homeVar = getPowerPoints2(home);
        awayVar = getPowerPoints2(away);
      }
      
      else if(var.equals("offenseDefense")) {
        homeVar = getPowerPoints2(home);
        awayVar = getPowerPoints2(away);
      }
      
      else if(var.equals("location")) {
        homeVar = 1;
        awayVar = 0;
        //System.out.println(home + ":\t" + homeVar);
      }
      
      else if(var.equals("location%")) {
        homeVar = home.getHomeWinPercent();
        awayVar = away.getAwayWinPercent();
      }
      
      else {
        throw new IllegalArgumentException("Sorry");
      }
      
      Team winner;
      if (homeVar == awayVar) winner = home;
      else if (homeVar > awayVar) winner = home;
      else winner = away;
      try {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputName, true)));
        writer.append(winner + ",");
        writer.flush();
        writer.close();
        //if (var.equals("location")) System.out.println("should be home:" + winner + "\tActual Winner:\t" + week8Results[i]);
        //if (winner.toString().equals(week8Results[i])) numCorrect++;
        System.out.println(winner + "\t" + game.getWinner());
        if (winner.equals(game.getWinner())){ numCorrect++; System.out.println("correct");}//
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }  
    
    try {
      PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputName, true)));
      writer.append("\n");
      writer.flush();
      writer.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println(var + "\t" + numCorrect + "\t" + totalGames);//
    System.out.println(var + ":\t" + numCorrect * 100 / totalGames + "% Correct");//
  }
  
  private int getRank(Team team) {
    for (int i = 0; i < allTeams.length; i++) {
      if (allTeams[i].equals(team)) return i + 1;
    }
    return -1;
  }
  
  private int getPowerPoints(Team team) {
    int powerPoints = 0;
    for (Game game : team.getGames())
      if (team.equals(game.getWinner())) {
        Team opp = game.getLoser();
        //System.out.println(team + " beat " + opp + " " + getRank(opp));
        powerPoints += 33 - getRank(opp);
      }
      else {
        Team opp = game.getWinner();
        powerPoints -= getRank(opp);
      }
    return powerPoints;
  }
  
  private double getPowerPoints2(Team team) {
    double powerPoints = 0;
    for (Game game : team.getGames())
      if (team.equals(game.getWinner())) {
        Team opp = game.getLoser();
        powerPoints += opp.getPointsAgainstPG();
      }
    return powerPoints;
  }
  
  public static void main(String[] args) {
    /*try {
      ParseNFLPreviousGames.main(new String[] {"8"});
      ParseNFLSchedule.main(new String[] {"9"});
    }
    catch (IOException io) {
      System.out.println("Something went wrong...");
    }*/
    int currentWeek = 9;
    
  
    CreateRankings cr = new CreateRankings(currentWeek - 1, currentWeek);
    Utilities util = new Utilities();
    //cr.printRankings();
    
    String outputName = util.createCSV("predict", currentWeek);
    cr.predictByVar("rank", outputName);
    cr.predictByVar("pointsFor", outputName);
    cr.predictByVar("pointsAgainst", outputName);
    cr.predictByVar("winPercent", outputName);
    cr.predictByVar("pointDiff", outputName);
    cr.predictByVar("powerPoints", outputName);
    cr.predictByVar("powerPoints2", outputName);
    cr.predictByVar("location", outputName);
    cr.predictByVar("location%", outputName);
    

    /*System.out.println("Games: " + cr.allGames.length);
    for (Game game : cr.allGames) System.out.println(game.toString());
    System.out.println("Games: " + upcomingGames.length);
    for (Game game : upcomingGames) System.out.println(game.toString()); */ 
  }
}