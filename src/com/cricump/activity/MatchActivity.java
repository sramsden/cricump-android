package com.cricump.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.cricump.R;
import com.cricump.data.*;
import com.cricump.net.Client;
import com.cricump.net.ClientCallback;

public class MatchActivity extends Activity {

    public static final String SHARED_PREFERENCES = "CricUmp";
    public static final String USER = "user";
    private ProgressDialog progressDialog;
    private String matchDescriptor;
    private boolean displayMoreHistory = false;
    private Button toggleHistoryButton;
    private Button refreshButton;
    private EditText userNameTextField;
    private Button userNameButton;
    private Match match;
    private LinearLayout userNameLayout;
    private LinearLayout chooseRunsLayout;
    private LinearLayout postWaffleLayout;
    private View signOut;
    private ScrollView scrollMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
        scrollMatch = (ScrollView) findViewById(R.id.scroll_match);
        toggleHistoryButton = (Button) findViewById(R.id.toggleHistory);
        refreshButton = (Button) findViewById(R.id.refresh);
        userNameTextField = (EditText) findViewById(R.id.user_name);
        userNameButton = (Button) findViewById(R.id.user_name_button);
        userNameLayout = (LinearLayout) findViewById(R.id.user_name_layout);
        chooseRunsLayout = (LinearLayout) findViewById(R.id.choose_runs_layout);
        postWaffleLayout = (LinearLayout) findViewById(R.id.post_waffle_layout);
        signOut = findViewById(R.id.sign_out);
        refresh(null);
    }

    public void toggleHistory(View view) {
        displayMoreHistory = !displayMoreHistory;
        if (displayMoreHistory) {
            if (historyListsInSync()) {
                displayHistoryAndRankings(match.getMoreHistoryItems());
            } else {
                refresh(view);
            }
        } else {
            if (historyListsInSync()) {
                displayHistoryAndRankings(match.getRecentHistoryItems());
            } else {
                refresh(view);
            }
        }
    }

    public void chooseRuns(View view) {
        startChooseRunsActivity(view);
    }

    public void chooseMatch(View view) {
        final Intent intent = new Intent(MatchActivity.this, MatchesActivity.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        SharedPreferences.Editor editor = getPrefsEditor();
        editor.remove(USER);
        editor.commit();
        userNameTextField.setText("");
        displayByUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_play_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                refresh(null);
                return true;
            case R.id.more_history:
                toggleHistory(null);
                return true;
            case R.id.other_matches:
                chooseMatch(null);
                return true;
            case R.id.sign_out:
                signOut(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private SharedPreferences.Editor getPrefsEditor() {
        SharedPreferences settings = getSharedPreferences(MatchActivity.SHARED_PREFERENCES, 0);
        return settings.edit();
    }

    private void startChooseRunsActivity(View view) {
        final Intent intent = new Intent(MatchActivity.this, ChooseRunsActivity.class);
        intent.putExtra("matchDescriptor", matchDescriptor);
        intent.putExtra("button", view.getId());
        startActivity(intent);
    }

    private boolean historyListsInSync() {
        boolean historyListsInSync = match != null && match.getMoreHistoryItems() != null && match.getRecentHistoryItems() != null
                && match.getMoreHistoryItems().length > 0 && match.getRecentHistoryItems().length > 0
                && last(match.getMoreHistoryItems()).equals(last(match.getRecentHistoryItems()));
        return historyListsInSync;
    }

    private HistoryItem last(HistoryItem[] historyItems) {
        return historyItems[historyItems.length - 1];
    }

    public void saveUserName(View view) {
        hideKeyboard(view);
        // save the user's name so that it can be used for posts..
        SharedPreferences.Editor editor = getPrefsEditor();
        String userName = userNameTextField.getText().toString();
        boolean user = userName != null && userName.length() > 0;
        if (user) {
            editor.putString(USER, userName);
            editor.commit();
        }
        displayByUser();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void postWaffle(final View view) {
        hideKeyboard(view);
        final String user = readUserName();
        final TextView waffleTextField = (TextView) findViewById(R.id.waffle);
        final String waffle = waffleTextField.getText().toString();
        if(user != null && user.length() > 0 && waffle != null && waffle.length() > 0 ){
            progressDialog = ProgressDialog.show(MatchActivity.this, "", "Submitting Waffle...", true);
            Client.postWaffle(match, user, waffle, new ClientCallback() {
                public void onSuccess(Object o) {
                    waffleTextField.setText("");
                    dismissProgressDialog();
                    displayMatch(match.getRecentHistoryItems());
                }

                public void onFailure(Object o) {
                    displayFailure();
                }
            });
        }
    }

    private void dismissProgressDialog() {
        // ensure match view is visible..
        scrollMatch.setVisibility(View.VISIBLE);
        progressDialog.dismiss();
    }

    private void displayHistoryItemWithoutReload(String content) {
        HistoryItem hi = new HistoryItem();
        hi.setCreatedAt("00:00.00");
        hi.setColour("#FFFFFF"); // white because we don't know ... TODO: find out
        hi.setContent(content);
        displayMatch(match.addRecentHistoryItem(hi));
    }

    private String readUserName() {
        SharedPreferences settings = getSharedPreferences(MatchActivity.SHARED_PREFERENCES, 0);
        return settings.getString(USER, null);
    }

    public void refresh(View view) {
        matchDescriptor = getIntent().getStringExtra("matchDescriptor");
        int predictedRuns = getIntent().getIntExtra("predictedRuns", -1);
        if(predictedRuns > -1 ){
            getIntent().removeExtra("predictedRuns");
            match = Cache.getMatch(matchDescriptor);
            displayMatch(match.getRecentHistoryItems());
        }
        else{
            progressDialog = ProgressDialog.show(MatchActivity.this, "", "Loading Match...", true);
            if (displayMoreHistory)
                loadAndDisplayAllHistory();
            else
                loadAndDisplayRecentHistory();
        }
        displayByUser();
    }

    private void displayByUser() {
        String userName = readUserName();
        boolean user = userName != null && userName.length() > 0;
        if (user) {
            userNameLayout.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);
            chooseRunsLayout.setVisibility(View.VISIBLE);
            postWaffleLayout.setVisibility(View.VISIBLE);
            final TextView choosePlayerLabel = (TextView) findViewById(R.id.choose_runs_label);
            choosePlayerLabel.setTextColor(Color.YELLOW);
            choosePlayerLabel.setText(userName + ", total runs off this over?");
        } else {
            userNameLayout.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.INVISIBLE);
            chooseRunsLayout.setVisibility(View.GONE);
            postWaffleLayout.setVisibility(View.GONE);
        }
    }

    private void loadAndDisplayRecentHistory() {
        Client.loadRecentMatchHistory(matchDescriptor, new ClientCallback() {
            public void onSuccess(Object o) {
                match = (Match) o;
                displayMatch(match.getRecentHistoryItems());
                dismissProgressDialog();
            }

            public void onFailure(Object o) {
                displayFailure();
            }
        });
    }


    private void loadAndDisplayAllHistory() {
        Client.loadAllMatchHistory(matchDescriptor, new ClientCallback() {

            public void onSuccess(Object o) {
                match = (Match) o;
                displayMatch(match.getMoreHistoryItems());
                dismissProgressDialog();
            }

            public void onFailure(Object o) {
                displayFailure();
            }
        });
    }

    private void displayFailure() {
        dismissProgressDialog();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Error Loading Match");
        alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                refresh(null);
            }
        });
        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                goToMatches();
            }
        });
        alertDialog.show();
    }

    private void goToMatches() {
        final Intent i = new Intent(MatchActivity.this, MatchesActivity.class);
        startActivity(i);
    }

    private void displayMatch(HistoryItem[] items) {
        displayHistoryAndRankings(items);
        displayByUser();
        scrollMatch.setVisibility(View.VISIBLE);
//        displayRunsSelect();
    }

    private void displayRunsSelect() {
        if(match == null)
            return;
        chooseRunsLayout.setVisibility(View.VISIBLE);
    }

    private void displayHistoryAndRankings(HistoryItem[] items) {
        displayHistoryItemsTable(items);
        displayRankingsTable();
        scrollMatch.post(new Runnable() {
            public void run() {
                scrollMatch.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void displayHistoryItemsTable(HistoryItem[] items) {
        TableLayout historyTable = (TableLayout) findViewById(R.id.history_table);
        historyTable.removeAllViews();

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(0, 0, 10, 0);

        for (HistoryItem item : items) {
            TextView itemTextView = new TextView(this);
            itemTextView.setText(Html.fromHtml(item.toString()));
            setHistoryItemTextSize(itemTextView);
            TableRow row = new TableRow(this);
            row.addView(itemTextView, params);
            historyTable.addView(row, params);
        }
    }

    private void displayRankingsTable() {
        TableLayout rankings = (TableLayout) findViewById(R.id.rankings);
        rankings.removeAllViews();

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(0, 0, 10, 0);

        TextView scoreH = new TextView(this);
        scoreH.setText(Html.fromHtml("<b>Score</b>"));
        TextView nameH = new TextView(this);
        nameH.setText(Html.fromHtml("<b>Name</b>"));
        TextView predictedH = new TextView(this);
        predictedH.setText(Html.fromHtml("<b>Latest Prediction</b>"));
        TableRow rowH = new TableRow(this);
        rowH.addView(scoreH, params);
        rowH.addView(nameH, params);
        rowH.addView(predictedH, params);
        rankings.addView(rowH);

        for (Ranking ranking : match.getRankings()) {
            TextView total = new TextView(this);
            total.setText(ranking.getTotal() + "");
            setRankingTextSize(total);
            TextView name = new TextView(this);
            name.setText(ranking.getUser());
            setRankingTextSize(name);
            TextView predicted = new TextView(this);
            predicted.setText(ranking.getLatestPrediction());
            setRankingTextSize(predicted);
            TableRow row = new TableRow(this);
            row.addView(total, params);
            row.addView(name, params);
            row.addView(predicted, params);
            rankings.addView(row, params);
        }
    }

    private void setHistoryItemTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
    }

    private void setRankingTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
    }
}

