package com.cricump.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.cricump.R;

public class CricUmpActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ImageView splash = (ImageView) findViewById(R.id.splash_img);
        splash.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                final Intent i = new Intent(CricUmpActivity.this, MatchesActivity.class);
                startActivity(i);
            }
        });

    }
}
