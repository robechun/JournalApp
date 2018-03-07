package com.example.robertchung.journalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "journal";
    private TextView mTextMessage;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    private String tempEntry;
    private Fragment fragment;
    private Fragment fragment_today;
    private Fragment fragment_history;
    private Fragment fragment_account;
    private FragmentManager fragManCreate;
    private FragmentTransaction fragTranCreate;
    Switch toggle;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragMan = getSupportFragmentManager();
            FragmentTransaction fragTran = fragMan.beginTransaction();
            //switches the fragment that is displayed based on navigation item selected
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = fragment_today;
                    fragTran.replace(R.id.fragment_place, fragment_today);
                    fragTran.commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragment = fragment_history;
                    fragTran.replace(R.id.fragment_place, fragment_history);
                    fragTran.commit();
                    return true;
                case R.id.navigation_notifications:
                    fragment = fragment_account;
                    fragTran.replace(R.id.fragment_place, fragment_account);
                    fragTran.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        //sets theme depending on users choice
        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
        else{
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //switch allows users to change the theme
        toggle = (Switch) findViewById(R.id.theme_switch);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });
        //if there is a saved state then it will pull the theme from there or else it will make new fragments
        if (savedInstanceState != null) {
            Boolean whichTheme = savedInstanceState.getBoolean("theme");
            toggle.setChecked(whichTheme);
        }
        else{
            fragment_today = new FragmentToday();
            fragment_history = new FragmentHistory();
            fragment_account = new FragmentAccount();
        }
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //retrieving fragment position and theme
        SharedPreferences sharedPref = getSharedPreferences("PREFS",0);
        //if a sharedPref has been initialized it will change the navigation selection to the appropriate one
        if(sharedPref.contains("initialized")) {
            String temp = sharedPref.getString("fragment_display", "");
            BottomNavigationView bottomNavigationView;
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
            switch (temp) {
                case "account":
                    bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
                    break;
                case "history":
                    bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
                    break;
                case "today":
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);

                   // ((FragmentToday) fragment).setEntry(sharedPref.getString("entry_temptext",""));
                    break;

            }
        }



    }
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
        Log.d(TAG, "onPause()");
        SharedPreferences sharedPref = getSharedPreferences("PREFS",0);
        SharedPreferences.Editor editor = sharedPref.edit();
        String whichFragment;
        //saves which type of fragment the last one that was on the screen before it paused was
        if (fragment instanceof FragmentAccount) {
            whichFragment = "account";
        }
        else if(fragment instanceof FragmentHistory) {
            whichFragment = "history";
        }
        else {
            whichFragment = "today";
            editor.putString("entry_temptext", ((FragmentToday) fragment).entryText());
        }
        editor.putBoolean("initialized",true);
        editor.putString("fragment_display", whichFragment);
        //commit edits to persistent storage (apply does this in the background)
        editor.apply();
    }
    private void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
