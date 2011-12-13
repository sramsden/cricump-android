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

public class MatchPlayersActivity extends ListActivity {

    private ProgressDialog progressDialog;
    private Match match;

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        String user = readUserName();
        // for simple list item of runs.. 0, 1, 2 etc. ... the runs value equals the position..
        progressDialog = ProgressDialog.show(MatchPlayersActivity.this, "", "Submitting Prediction...", true);
        Client.postPrediction(match, user, position, new ClientCallback() {
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                final Intent intent = new Intent(MatchPlayersActivity.this, MatchActivity.class);
                intent.putExtra("matchDescriptor", match.getDescriptor());
                intent.putExtra("predictedRuns", position);
                startActivity(intent);
            }

            public void onFailure(Object o) {
                displayFailure();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        match = Cache.getMatch(getIntent().getStringExtra("matchDescriptor"));
        init();
    }

    private void init() {
        String[] options = new String[50];
        for(int i=0; i < 50; i++){
            options[i] = i + "";
        }
        this.setListAdapter(
                new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_1, options));
    }

    private void displayFailure() {
        progressDialog.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Error Submitting Prediction");
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
        final Intent i = new Intent(MatchPlayersActivity.this, CricUmpActivity.class);
        startActivity(i);
    }

    private String readUserName() {
        SharedPreferences settings = getSharedPreferences(MatchActivity.SHARED_PREFERENCES, 0);
        return settings.getString(MatchActivity.USER, null);
    }

}

