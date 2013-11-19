package com.cricump.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.cricump.R;
import com.cricump.data.Cache;
import com.cricump.data.Match;
import com.cricump.net.Client;
import com.cricump.net.ClientCallback;
import com.cricump.util.CancelableDialog;

public class CannedMessageActivity extends ListActivity {

    private CancelableDialog progressDialog;
    private Match match;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String user = readUserName();
        progressDialog = CancelableDialog.show(CannedMessageActivity.this, getApplicationContext(), "Submitting...");
        ClientCallback clientCallback = new ClientCallback() {
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                final Intent intent = new Intent(CannedMessageActivity.this, MatchActivity.class);
                intent.putExtra("matchDescriptor", match.getDescriptor());
                intent.putExtra("predictedRuns", -1); // greater than -2 is an indicator not to reload entire match .... why??
                startActivity(intent);
            }


            public void onFailure(Object o) {
                displayFailure();
            }
        };
        Client.postWaffle(match, user, Cache.getCannedMessages()[position], clientCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        match = Cache.getMatch(getIntent().getStringExtra("matchDescriptor"));
        init();
    }

    private void init() {
        progressDialog = CancelableDialog.show(CannedMessageActivity.this, getApplicationContext(), "Loading Canned Messages...");
        String[] cannedMessages = Cache.getCannedMessages();
        if (cannedMessages != null) {
            displayCannedMessages();
        } else {
            loadAndDisplayCannedMessages();
        }
    }

    private void loadAndDisplayCannedMessages() {
        Client.loadCannedMessages(new ClientCallback() {

            public void onSuccess(Object o) {
                displayCannedMessages();
            }


            public void onFailure(Object o) {
                displayFailure();
            }
        });
    }

    private void displayCannedMessages() {
        this.setListAdapter(
                new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_1, Cache.getCannedMessages() ));
        progressDialog.dismiss();
    }

    private void displayFailure() {
        progressDialog.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Error Posting Waffle");
        alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                init();
            }
        });
        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                goToSplash();
            }
        });
        alertDialog.show();
    }

    private void goToSplash() {
        final Intent i = new Intent(CannedMessageActivity.this, CricUmpActivity.class);
        startActivity(i);
    }

    private String readUserName() {
        SharedPreferences settings = getSharedPreferences(MatchActivity.SHARED_PREFERENCES, 0);
        return settings.getString(MatchActivity.USER, null);
    }

}

