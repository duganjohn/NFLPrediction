import java.util.*;

class Team implements Comparable {
  private String abbrName;
  private int gamesPlayed, wins, losses;
  private int pointsFor, pointsAgainst;
  private double pointsForPG, pointsAgainstPG, pointSpreadPG, pointDiffPG, pointsSpreadPG;
  private double winPercent, homeWinPercent, awayWinPercent; 
  private ArrayList<Game> games;

  public Team(String abbrName) {
    this.abbrName = abbrName;
    this.games = new ArrayList<Game>();
    updatePPG();
  }
  
  private void updatePPG() {
    this.gamesPlayed = wins + losses;
    if (gamesPlayed == 0) {
      this.pointsForPG = 0;
      this.pointsAgainstPG = 0;
      this.pointSpreadPG = 0;
      this.winPercent = 0;
      this.homeWinPercent = 0;
      this.awayWinPercent = 0;
      this.pointDiffPG = 0;
    }
    else {
      this.pointsForPG = Math.round((double)pointsFor / gamesPlayed * 10) / 10.0;
      this.pointsAgainstPG = Math.round((double)pointsAgainst / gamesPlayed * 10) / 10.0;
      //this.pointSpreadPG = (double)(pointsFor - pointsAgainst) / gamesPlayed; // Per game, not overall
      calculateWinPercents();
      this.pointDiffPG = pointsForPG - pointsAgainstPG;
    }
  }
  
  private void calculateWinPercents() {
    this.winPercent = Math.round((double)wins / gamesPlayed * 10) / 10.0;
    
    int homeGames = 0, awayGames = 0, homeWins = 0, awayWins = 0;
    for (Game game : games) {
      if (this.equals(game.getHomeTeam())) {
        homeGames++;
        if (this.equals(game.getWinner())) {
          homeWins++;
        }
      }
      else {
        awayGames++;
        if (this.equals(game.getWinner())) {
          awayWins++;
        }
      }
    }
    this.homeWinPercent = Math.round((double)homeWins / homeGames * 10) / 10.0;
    this.awayWinPercent = Math.round((double)awayWins / awayGames * 10) / 10.0;
  }
  
  public void addGame(Game game) {
    games.add(game);
    
    if (game.getHomeTeam().equals(this)) {  // Home Game
      addPointsFor(game.getHomeScore());
      addPointsAgainst(game.getAwayScore());
    }
    
    else {  // Away Game
      addPointsFor(game.getAwayScore());
      addPointsAgainst(game.getHomeScore());
    }    
  }
  
  public void addWin() {
    wins++;
    updatePPG();
  }
  
  public void addLoss() {
    losses++;
    updatePPG();
  }
  
  public void addPointsFor(int points) {
    pointsFor += points;
    updatePPG();
  }  

  public void addPointsAgainst(int points) {
    pointsAgainst += points;
    updatePPG();
  }
  
  public int getWins() {
    return wins;
  }
  
  public int getLosses() {
    return losses;
  }
  
  public int getPointsFor() {
    return pointsFor;
  }
  
  public int getPointsAgainst() {
    return pointsAgainst;
  }
  
  public int getGamesPlayed() {
    return gamesPlayed;
  }
  
  public double getPointsForPG() {
    return pointsForPG;
  }
  
  public double getPointsAgainstPG() {
    return pointsAgainstPG;
  }
  
  public double getPointDiffPG() {
    return pointDiffPG;
  }

  public double getWinPercent() {
    return winPercent;
  }
  
  public double getHomeWinPercent() {
    return homeWinPercent;
  }  
  
  public double getAwayWinPercent() {
    return awayWinPercent;
  }  
  
  public Game[] getGames() {
    return (Game[])games.toArray(new Game[games.size()]);
  }

  public String getAbbrName() {
    return abbrName;
  }
  
  public String toString() {
    return abbrName;
  }
  
  @Override
  public int compareTo(Object o) {
    //return this.toString().compareTo(o.toString());
    Team other = (Team) o;
    
    int ret = other.wins - this.wins;
    if (ret == 0) ret = this.losses - other.losses;
    if (ret == 0) ret = (int)(other.pointDiffPG - this.pointDiffPG);
    
    return ret;
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Team)) return false;
    
    Team team = (Team)other;
    return this.getAbbrName().equals(team.getAbbrName());
  }
}