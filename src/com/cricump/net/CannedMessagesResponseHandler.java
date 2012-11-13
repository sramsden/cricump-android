package com.cricump.net;

import com.loopj.android.http.JsonHttpResponseHandler;

import com.cricump.data.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CannedMessagesResponseHandler extends JsonHttpResponseHandler {

    ClientCallback clientCallback;

    public CannedMessagesResponseHandler(ClientCallback clientCallback) {
        this.clientCallback = clientCallback;
    }

    public void onSuccess(org.json.JSONArray jsonArray) {
        try {
            int length = jsonArray.length();
            String[] cannedMessages = new String[length];
            for (int i = 0; i < length; i++) {
                cannedMessages[i] = jsonArray.getString(i);
            }
//            ArrayList<String> list = new ArrayList<String>();
//            jsonArray = cannedMessagesJson;
//            if (jsonArray != null) {
//                int len = jsonArray.length();
//                for (int i = 0; i < len; i++) {
//                    list.add(jsonArray.get(i).toString());
//                }
//            }
//            Cache.setCannedMessages((String[]) list.toArray());
            Cache.setCannedMessages(cannedMessages);
            clientCallback.onSuccess("CannedMessages Loaded");
        } catch (Exception e) {
            onFailure(e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        clientCallback.onFailure(throwable);
    }
}
