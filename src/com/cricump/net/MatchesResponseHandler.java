package com.cricump.net;

import com.cricump.activity.MatchesActivity;
import com.cricump.data.Cache;
import com.cricump.data.Match;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchesResponseHandler extends JsonHttpResponseHandler {

    ClientCallback _clientCallback;

    public MatchesResponseHandler(ClientCallback clientCallback) {
        _clientCallback = clientCallback;
    }

    public void onSuccess(org.json.JSONArray jsonArray) {
        try {
            int matchCount = jsonArray.length();
            String[] strings = new String[matchCount + 1];//{"foo vs. bar", "baz vs foo", "bar vs baz", "REFRESH MATCH LIST"};
            for (int i = 0; i < matchCount; i++) {
                final JSONObject matchJson = jsonArray.getJSONObject(i);
                final String descriptor = matchJson.getString("descriptor");
                final String url = matchJson.getString("url");
                final long id = matchJson.getLong("id");
                Match match = new Match(id, descriptor, url);
                Cache.setMatch(descriptor,match);
                strings[i] = descriptor;
            }
            strings[matchCount] = MatchesActivity.REFRESH_MATCHES;
            Cache.setMatchDescriptors(strings);
            _clientCallback.onSuccess("Matches Loaded");
        } catch (JSONException e) {
            onFailure(e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        _clientCallback.onFailure(throwable);
    }
}
