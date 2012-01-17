package com.cricump.net;

import com.cricump.data.Cache;
import com.cricump.data.Match;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;

public class MatchResponseHandler extends JsonHttpResponseHandler {

    ClientCallback clientCallback;

    public MatchResponseHandler(ClientCallback clientCallback) {
        this.clientCallback = clientCallback;
    }

    public void onSuccess(org.json.JSONObject matchJson) {
        try {
            final String descriptor = matchJson.getString("descriptor");
            final Match match = Cache.getMatch(descriptor);
            // don't need to set id or url but will set rest of values...
            match.setScheduledStart(matchJson.getString("scheduled_start"));
//            match.setScheduledStartString(matchJson.getString("scheduled_start_string"));
            match.setCommentaryUrl(matchJson.getString("commentary_url"));
            match.setMatchesUrl(matchJson.getString("matches_url"));
            match.setRankingUrl(matchJson.getString("ranking_url"));
            match.setCommentaryAndRankingUrl(matchJson.getString("commentary_and_ranking_url"));
            Cache.setMatch(descriptor, match);
            clientCallback.onSuccess("Match Loaded");
        } catch (JSONException e) {
            onFailure(e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        clientCallback.onFailure(throwable);
    }
}
