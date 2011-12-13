package com.cricump.net;

import com.cricump.data.HistoryItem;
import com.cricump.data.Match;
import com.cricump.data.Ranking;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractMatchHistoryResponseHandler extends JsonHttpResponseHandler {
    ClientCallback clientCallback;
    Match match;

    public AbstractMatchHistoryResponseHandler(Match match, ClientCallback clientCallback) {
        this.match = match;
        this.clientCallback = clientCallback;
    }

    protected HistoryItem[] parseHistoryItems(org.json.JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        HistoryItem[] historyItems = new HistoryItem[length];
        for (int i = 0; i < length; i++) {
            final JSONObject itemJson = jsonArray.getJSONObject(i);
            HistoryItem item = new HistoryItem();
            item.setCreatedAt(itemJson.getString("created_at"));
            item.setColour(itemJson.getString("colour"));
            item.setContent(itemJson.getString("content"));
            historyItems[i] = item;
        }
        return historyItems;
    }

    protected Ranking[] parseRankings(org.json.JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        Ranking[] rankings = new Ranking[length];
        for (int i = 0; i < length; i++) {
            final JSONObject itemJson = jsonArray.getJSONObject(i);
            rankings[i] = new Ranking(
                    itemJson.getString("user"), itemJson.getInt("total"), itemJson.getString("latest_prediction"));
        }
        return rankings;
    }

    @Override
    public void onFailure(Throwable throwable) {
        clientCallback.onFailure(throwable);
    }
}
