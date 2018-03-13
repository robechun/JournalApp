package com.example.robertchung.journalapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class FragmentAccount extends Fragment {

    private Button logout;

    private FirebaseAuth mAuth;
    private AlarmManager alarm_manager;
    private TimePicker timepicker;
    private Button on;
    private Button off;
    AlarmManager manager;
    PendingIntent pendingIntent;
    Intent alarmIntent;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);


    }

    @Override
    public void onPause() {
        super.onPause();
        //stores text in shared preferences
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("hour", timepicker.getHour());
        editor.putInt("minute",timepicker.getMinute());
        editor.putBoolean("initialized",true);
        //commit edits to persistent storage (apply does this in the background)
        editor.apply();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        alarmIntent = new Intent(this.getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.getActivity(), 0, alarmIntent, 0);

        manager = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);

        logout = (Button) getView().findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        on = (Button) getView().findViewById(R.id.on);
        off = (Button) getView().findViewById(R.id.off);
        timepicker = (TimePicker) getView().findViewById(R.id.timePicker);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        if(sharedPref.contains("initialized")) {
            timepicker.setHour(sharedPref.getInt("hour",0));
            timepicker.setMinute(sharedPref.getInt("minute",0));
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                getActivity().onBackPressed();
            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationOn(timepicker.getHour(),timepicker.getMinute());
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationOff();
            }
        });

    }
    private void notificationOn(int hour, int minute) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 1);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(getActivity().getApplicationContext(), "Alarm Set.", Toast.LENGTH_SHORT).show();

    }
    private void notificationOff() {
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 1253, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getActivity().getApplicationContext(), "Alarm Off.", Toast.LENGTH_SHORT).show();

    }

}
