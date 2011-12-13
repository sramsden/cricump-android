package com.cricump.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
public class TeamMatchPlayers {

    private String name;
    private MatchPlayer[] matchPlayersByName;
    private MatchPlayer[] matchPlayersByNumber;

    public TeamMatchPlayers(String name, MatchPlayer[] matchPlayersByName, MatchPlayer[] matchPlayersByNumber) {
        this.name = name;
        this.matchPlayersByName = matchPlayersByName;
        this.matchPlayersByNumber = matchPlayersByNumber;
    }

    public String getName() {
        return name;
    }

    public MatchPlayer[] getMatchPlayersByName() {
        return matchPlayersByName;
    }

    public MatchPlayer[] getMatchPlayersByNumber() {
        return matchPlayersByNumber;
    }

    public static TeamMatchPlayers parse(JSONObject json) throws JSONException {
        String name = json.getString("name");
        MatchPlayer[] byNames = parseMatchPlayersJson(json.getJSONArray("match_players_by_name"));
        MatchPlayer[] byNumbers = parseMatchPlayersJson(json.getJSONArray("match_players_by_number"));
        return new TeamMatchPlayers(name, byNames, byNumbers);
    }

    private static MatchPlayer[] parseMatchPlayersJson(JSONArray matchPlayersJson) throws JSONException {
        final MatchPlayer[] matchPlayers;
        final int length = matchPlayersJson.length();
        matchPlayers = new MatchPlayer[length];
        for (int i = 0; i < length; i++) {
            final JSONObject matchPlayerJson = matchPlayersJson.getJSONObject(i);
            final int id = matchPlayerJson.getInt("id");
            final String descriptor = matchPlayerJson.getString("descriptor");
            matchPlayers[i] = new MatchPlayer(id, descriptor);
        }
        return matchPlayers;
    }
}
