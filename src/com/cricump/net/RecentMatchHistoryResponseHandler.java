package com.cricump.net;

import com.cricump.data.Match;
import org.json.JSONException;

public class RecentMatchHistoryResponseHandler extends AbstractMatchHistoryResponseHandler {

    public RecentMatchHistoryResponseHandler(Match match, ClientCallback clientCallback) {
        super(match, clientCallback);
    }

    public void onSuccess(org.json.JSONObject json) {
        try {
            readRecentCommentaryJson(json);
            readRankingJson(json); // can only do when we are reading from commentary_and_ranking_url !!!
            clientCallback.onSuccess(match);
        } catch (JSONException e) {
            onFailure(e);
            e.printStackTrace();
        }
    }

}
