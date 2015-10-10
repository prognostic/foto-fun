package com.prognosticator.funme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    CheckBox checkBox;
    final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (loadSettings()) {
            // FIXME
//            Intent intent = new Intent(this, CreatingFunActivity.class);
//            startActivity(intent);
//            Log.d(LOG_TAG, "перешли на создание фана");
//            finish();
        }
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        checkBox = (CheckBox) findViewById(R.id.I_can);
        checkBox.setOnClickListener(this);

        // display an icon in the Action bar
        // ActionBar actionBar = getSupportActionBar();
        // actionBar.setLogo(R.drawable.top_bar_logo);
        // actionBar.setDisplayUseLogoEnabled(true);
        // actionBar.setDisplayShowHomeEnabled(true);
    }

    /**
     * Called when the user clicks the Tutorial button
     */
    public void goToTutorial(View view) {
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Fun button
     */
    public void goAndCreateAFan(View view) {
        Intent intent = new Intent(this, CreatingFunActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.I_can:
                if (checked)
                    saveSettings(true);
                else
                    saveSettings(false);
                break;
            default:
                break;
        }
    }

    void saveSettings(boolean selection) {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("show_a_welcome_screen", selection);
        editor.apply();
        if (selection) {
            Toast.makeText(this, "Этот экран больше не будет показан при старте приложения", Toast.LENGTH_SHORT).show();
        }
    }

    boolean loadSettings() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getBoolean("show_a_welcome_screen", true);
    }

}
