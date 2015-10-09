package com.prognosticator.funme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // display an icon in the Action bar
        // ActionBar actionBar = getSupportActionBar();
        // actionBar.setLogo(R.drawable.top_bar_logo);
        // actionBar.setDisplayUseLogoEnabled(true);
        // actionBar.setDisplayShowHomeEnabled(true);
    }

    /** Called when the user clicks the Tutorial button */
    public void goToTutorial(View view) {
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Fun button */
    public void goAndCreateAFan(View view) {
        Intent intent = new Intent(this, CreatingFunActivity.class);
        startActivity(intent);
    }

}
