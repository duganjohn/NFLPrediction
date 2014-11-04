class Game implements Comparable {
  private Team homeTeam;
  private Team awayTeam;
  private int homeScore;
  private int awayScore;
  private Team winner;
  private Team loser;
  private int week;
  
  public Game(Team awayTeam, Team homeTeam) {
    this(20, awayTeam, homeTeam, 0, 0);
  }
  
  public Game(int week, Team awayTeam, Team homeTeam, int awayScore, int homeScore) {
    this.week = week;
    this.awayTeam = awayTeam;
    this.homeTeam = homeTeam;
    this.awayScore = awayScore;
    this.homeScore = homeScore;

    if (homeScore > awayScore) {
      this.winner = homeTeam;
      this.loser = awayTeam;
    }
    else {
      this.winner = awayTeam;
      this.loser = homeTeam;
    }
  }
  
  public Team getWinner() {
    return winner;
  }
  
  public Team getLoser() {
    return loser;
  }
  
  public int getWinningScore() {
    return Math.max(homeScore, awayScore);
  }
  
  public int getLosingScore() {
    return Math.min(homeScore, awayScore);
  }
  
  public Team getHomeTeam() {
    return homeTeam;
  }
  
  public Team getAwayTeam() {
    return awayTeam;
  }
  
  public int getWeek() {
    return week;
  }
  
  public int getHomeScore() {
    return homeScore;
  }
  
  public int getAwayScore() {
    return awayScore;
  }
  
  public String toString() {
    if (getWinningScore() == getLosingScore()) return winner.toString() + " and " + loser.toString() + " tied!";
    return winner.toString() + " defeated " + loser.toString() + " " + getWinningScore() + "-" + getLosingScore();
  }
  
  @Override
  public int compareTo(Object o) {
    return this.toString().compareTo(o.toString());
  }
  
  @Override
  public boolean equals(Object o) {
    return this.toString().compareTo(o.toString()) == 0;
  }
}