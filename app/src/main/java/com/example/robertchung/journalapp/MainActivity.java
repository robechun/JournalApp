package com.example.robertchung.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            FragmentManager fragMan = getFragmentManager();
            FragmentTransaction fragTran = fragMan.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new FragmentToday();
                    fragTran.replace(R.id.fragment_place, fragment);
                    fragTran.commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new FragmentHistory();
                    fragTran.replace(R.id.fragment_place, fragment);
                    fragTran.commit();
                    return true;
                case R.id.navigation_notifications:
                    fragment = new FragmentAccount();
                    fragTran.replace(R.id.fragment_place, fragment);
                    fragTran.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
