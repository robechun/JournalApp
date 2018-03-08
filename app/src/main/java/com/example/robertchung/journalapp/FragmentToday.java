package com.example.robertchung.journalapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

public class FragmentToday extends Fragment {

    private static final String KEY_ADAPTER_STATE = "KEY_ADAPTER_STATE";
    private EditText entry;
    private String entryText;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // Put in whatever they have into the database
    Calendar calendar;
    private String userUID;
    private String dateFormatted;


    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        calendar = Calendar.getInstance();
        dateFormatted = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()).replaceAll("/","");

        userUID = mAuth.getCurrentUser().getUid();

        //retrieves entryText from onSaved
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ADAPTER_STATE)){
            entryText = savedInstanceState.getString(KEY_ADAPTER_STATE, "");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        entry = (EditText) getView().findViewById(R.id.entry_edit);

        /*
        //sets text to entryText if savedInstance exists
        if (savedInstanceState != null) {
            entry.setText(entryText, TextView.BufferType.EDITABLE);

        }*/

        // Grab from database!!!
        if (mDatabase != null){
            mDatabase.child("Users").child(userUID).child("Entries").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.child(dateFormatted).exists()) {
                            Map<String, Object> m = (Map<String, Object>) snapshot.getValue();
                            System.out.println("YO" + m.get(dateFormatted));
                            entry.setText(m.get(dateFormatted).toString());
                        } else {
                            entry.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        } else {
            //sets entry.text to data from onPause
            SharedPreferences sharedPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            if (sharedPref.contains("initialized")) {
                entry.setText(sharedPref.getString("text", ""), TextView.BufferType.EDITABLE);
            }
            TextView textViewDate = (TextView) getView().findViewById(R.id.text_view_date);
            textViewDate.setText(dateFormatted);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_ADAPTER_STATE, entry.getText().toString());

    }

    @Override
    public void onPause() {
        super.onPause();

        System.out.println("IN ON PAUSE");

        //stores text in shared preferences
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("text", entryText());
        editor.putBoolean("initialized",true);




        // Check to see if an entry exists
        // If it exists, update the entry with current
        // If it does not exist, create a new journal entry
        mDatabase.child("Users").child(userUID).child("Entries").
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(dateFormatted).exists()) {
                    mDatabase.child("Users").child(userUID).child("Entries").child(dateFormatted).setValue(entryText());

                } else {
                    // Make a new journal object and send it to database
                    Journal newEntry = new Journal(entryText());

                    // Send to database
                    mDatabase.child("Users").child(userUID).child("Entries").child(dateFormatted).setValue(newEntry);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //commit edits to persistent storage (apply does this in the background)
        editor.apply();
    }

    public String entryText() {
        return entry.getText().toString();
    }

    public void setEntry(String text_entry) {
        this.entry.setText(text_entry,TextView.BufferType.EDITABLE);
    }
}




