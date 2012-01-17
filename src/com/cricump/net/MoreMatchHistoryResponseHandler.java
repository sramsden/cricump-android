package com.cricump.net;

import com.cricump.data.Match;
import org.json.JSONException;

public class MoreMatchHistoryResponseHandler extends AbstractMatchHistoryResponseHandler {

    public MoreMatchHistoryResponseHandler(Match match, ClientCallback clientCallback) {
        super(match, clientCallback);
    }

    public void onSuccess(org.json.JSONObject json) {
        try {
            readMoreCommentaryJson(json);
            clientCallback.onSuccess(match);
        } catch (JSONException e) {
            onFailure(e);
            e.printStackTrace();
        }
    }

}
