package com.example.robertchung.journalapp;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class FragmentToday extends Fragment {
    private EditText entry;
    View view;
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

        TextView textViewDate = (TextView) getView().findViewById(R.id.text_view_date);
        textViewDate.setText(currentDate);
    }



    }




