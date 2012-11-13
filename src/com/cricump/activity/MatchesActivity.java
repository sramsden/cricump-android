package com.cricump.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.cricump.data.Cache;
import com.cricump.net.Client;
import com.cricump.net.ClientCallback;

public class MatchesActivity extends ListActivity {

    public static final String REFRESH_MATCHES = "REFRESH MATCH LIST";
    private ProgressDialog progressDialog;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final String matchDescriptor = ((TextView) v).getText().toString();
        if(matchDescriptor.equals(REFRESH_MATCHES) ){
            Cache.setMatchDescriptors(null);
            init();
        }
        else {
            final Intent intent = new Intent(MatchesActivity.this, MatchActivity.class);
            intent.putExtra("matchDescriptor", matchDescriptor);
            Cache.removeCannedMessages(); // ensure canned messages list refreshed also
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        progressDialog = ProgressDialog.show(MatchesActivity.this, "", "Loading Matches...", true);
        final String[] descriptors = Cache.getMatchDescriptors();
        if (descriptors != null) {
            displayMatches(descriptors);
        } else {
            loadAndDisplayMatches();
        }
    }

    private void loadAndDisplayMatches() {
        Client.loadMatches(new ClientCallback() {
            public void onSuccess(Object o) {
                final String[] descriptors = Cache.getMatchDescriptors();
                displayMatches(descriptors);
            }

            public void onFailure(Object o) {
                displayFailure();
            }
        });
    }

    private void displayFailure() {
        progressDialog.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Error Loading Matches");
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
        final Intent i = new Intent(MatchesActivity.this, CricUmpActivity.class);
        startActivity(i);
    }

    private void displayMatches(String[] descriptors) {
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, descriptors));
        progressDialog.dismiss();
        // once displayed wait for ... onListItemClick
    }
}

