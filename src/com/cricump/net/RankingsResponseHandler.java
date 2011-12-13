package com.cricump.net;

import com.cricump.data.Match;
import com.cricump.data.Ranking;
import org.json.JSONArray;
import org.json.JSONException;

public class RankingsResponseHandler extends AbstractMatchHistoryResponseHandler {

    public RankingsResponseHandler(Match match, ClientCallback clientCallback) {
        super(match, clientCallback);
    }

    public void onSuccess(org.json.JSONObject json) {
        try {
            final JSONArray rankings = json.getJSONArray("ranking");
            Ranking[] parsedRankings = parseRankings(rankings);
            boolean noExistingRankings = match.getRankings() == null || match.getRankings().length == 0;
            boolean rankingsRetrieved = parsedRankings != null && parsedRankings.length > 0;
            if(noExistingRankings || rankingsRetrieved ){
                match.setRankings(parsedRankings);
            }
            clientCallback.onSuccess(match);
        } catch (JSONException e) {
            onFailure(e);
            e.printStackTrace();
        }
    }

}
