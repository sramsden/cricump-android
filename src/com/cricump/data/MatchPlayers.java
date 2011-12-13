package com.cricump.data;

public class MatchPlayers {

    private Match match;
    private TeamMatchPlayers home;
    private TeamMatchPlayers away;

    public MatchPlayers(Match match, TeamMatchPlayers home, TeamMatchPlayers away) {
        this.match = match;
        this.home = home;
        this.away = away;
    }

    public Match getMatch() {
        return match;
    }

    public TeamMatchPlayers getHome() {
        return home;
    }

    public TeamMatchPlayers getAway() {
        return away;
    }

    public MatchPlayer[] all() {
        return concat(concat(home.getMatchPlayersByName(), home.getMatchPlayersByNumber()),
                concat(away.getMatchPlayersByName(), away.getMatchPlayersByNumber()));
    }

    private MatchPlayer[] concat(MatchPlayer[] a, MatchPlayer[] b) {
        MatchPlayer[] c = new MatchPlayer[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
