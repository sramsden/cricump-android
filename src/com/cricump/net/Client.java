package com.cricump.net;

import com.cricump.data.Cache;
import com.cricump.data.HistoryItem;
import com.cricump.data.Match;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;

public class Client {

    // for some reason local ip 192.168.0.12:3000 not working .. but using gateway port forwarding can use ...
    private static final String BASE_URL = "http://114.77.27.130:3000"; // can set to :3001 to simulate connection refused
//    private static final String BASE_URL = "http://cricket.goalump.com";
    private static final AsyncHttpClient CLIENT = new AsyncHttpClient();


    private static void get(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String url = getAbsoluteUrl(relativeUrl);
        CLIENT.get(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void loadMatches(ClientCallback clientCallback) {
        get("/apis/matches.json", null, new MatchesResponseHandler(clientCallback));
    }

    public static void loadRecentMatchHistory(final String descriptor, final ClientCallback clientCallback) {
        Match match = Cache.getMatch(descriptor);
        if(match.getCommentaryUrl() == null ){
            CLIENT.get(match.getUrl(), null, new MatchResponseHandler(new ClientCallback() {
                public void onSuccess(Object o) {
                    loadRecentMatchHistoryDirectly(descriptor, clientCallback);
                }

                public void onFailure(Object o) {
                  clientCallback.onFailure(o);
                }
            }));
        }
        else{
            loadRecentMatchHistoryDirectly(descriptor,clientCallback);
        }
    }

    public static void loadMatchRankings(final String descriptor, final ClientCallback clientCallback) {
        final Match match = Cache.getMatch(descriptor);
        if(match.getRankingUrl() == null ){
            CLIENT.get(match.getUrl(), null, new MatchResponseHandler(new ClientCallback() {
                public void onSuccess(Object o) {
                    CLIENT.get(match.getRankingUrl(), null, new RankingsResponseHandler(match, clientCallback));
                }

                public void onFailure(Object o) {
                  clientCallback.onFailure(o);
                }
            }));
        }
        else{
            CLIENT.get(match.getRankingUrl(), null, new RankingsResponseHandler(match, clientCallback));
        }
    }

    private static void loadRecentMatchHistoryDirectly(String descriptor, ClientCallback clientCallback) {
        Match match = Cache.getMatch(descriptor);
        HistoryItem[] recentHistoryItems = match.getRecentHistoryItems();
        String since = null;
        if(recentHistoryItems != null && recentHistoryItems.length > 0){
            HistoryItem last = recentHistoryItems[recentHistoryItems.length - 1];
            since = last.getCreatedAt();
        }
        RequestParams params = new RequestParams("since", since);
        CLIENT.get(match.getCommentaryUrl(), params, new RecentMatchHistoryResponseHandler(match, clientCallback));
    }

    public static void loadAllMatchHistory(final String descriptor, final ClientCallback clientCallback) {
        // we'll get the match fresh but need the url from the cached match...
        Match match = Cache.getMatch(descriptor);
        CLIENT.get(match.getUrl(), null, new MatchResponseHandler(new ClientCallback() {
            public void onSuccess(Object o) {
                Match match = Cache.getMatch(descriptor);
                RequestParams params = new RequestParams("history_limit", "100");
                CLIENT.get(match.getCommentaryUrl(), params, new MoreMatchHistoryResponseHandler(match, clientCallback));
            }

            public void onFailure(Object o) {
              clientCallback.onFailure(o);
            }
        }));
    }

    public static void postPrediction(final Match match, final String user, final int runs, final ClientCallback clientCallback){

        if(user == null || user.length() == 0){
            clientCallback.onSuccess("User not specified");
            return;
        }
        RequestParams params = new RequestParams();

        params.put("id", match.getId() + "");
        params.put("user", user);
        params.put("runs", runs + "");
        CLIENT.post(getAbsoluteUrl("/apis/add_prediction"), params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject jsonObject) {
                clientCallback.onSuccess("Prediction Submitted");
            }

            @Override
            public void onFailure(Throwable throwable) {
                clientCallback.onFailure(throwable);
            }
        });
    }

    public static void postWaffle(final Match match, final String user, final String waffle, final ClientCallback clientCallback){

        if(user == null || user.length() == 0){
            clientCallback.onSuccess("User not specified");
            return;
        }
        RequestParams params = new RequestParams();

        params.put("id", match.getId() + "");
        params.put("user", user);
        params.put("waffle", waffle);
        CLIENT.post(getAbsoluteUrl("/apis/add_waffle"), params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject jsonObject) {
                clientCallback.onSuccess("Waffle Submitted");
            }

            @Override
            public void onFailure(Throwable throwable) {
                clientCallback.onFailure(throwable);
            }
        });
    }



}

