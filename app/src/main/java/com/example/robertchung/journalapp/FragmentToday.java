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

import java.text.DateFormat;
import java.util.Calendar;

public class FragmentToday extends Fragment {
    private static final String KEY_ADAPTER_STATE = "KEY_ADAPTER_STATE";
    private EditText entry;
    private String entryText;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        entry = (EditText) getView().findViewById(R.id.entry_edit);
        if (savedInstanceState != null) {
            entry.setText(entryText, TextView.BufferType.EDITABLE);

        }
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        if(sharedPref.contains("initialized")) {
            entry.setText(sharedPref.getString("text",""), TextView.BufferType.EDITABLE);
        }
        TextView textViewDate = (TextView) getView().findViewById(R.id.text_view_date);
        textViewDate.setText(currentDate);
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
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("text", entryText());
        editor.putBoolean("initialized",true);
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




