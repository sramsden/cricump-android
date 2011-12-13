package com.cricump.net;

import com.cricump.data.Match;
import com.cricump.data.Ranking;
import org.json.JSONArray;
import org.json.JSONException;

public class RecentMatchHistoryResponseHandler extends AbstractMatchHistoryResponseHandler {

    public RecentMatchHistoryResponseHandler(Match match, ClientCallback clientCallback) {
        super(match, clientCallback);
    }

    public void onSuccess(org.json.JSONObject json) {
        try {
            final JSONArray history = json.getJSONArray("commentary");
            match.setRecentHistoryItems(parseHistoryItems(history));
//            match.addRecentHistoryItems(parseHistoryItems(history)); // TODO: use add when fetching history 'since'
            clientCallback.onSuccess(match);
        } catch (JSONException e) {
            onFailure(e);
            e.printStackTrace();
        }
    }

}
