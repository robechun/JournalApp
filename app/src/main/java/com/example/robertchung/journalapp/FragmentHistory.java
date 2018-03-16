package com.example.robertchung.journalapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FragmentHistory extends Fragment {
    private final int REQUEST_TEXT = 0;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String userUID;


    private Map<String,Object> entries;
    private List<String> listOfDates;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userUID = mAuth.getCurrentUser().getUid();

        System.out.println("HELLOD EBU1231232G");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Grab all the entries for that user
        if (mDatabase != null) {
            mDatabase.child("Users").child(userUID).child("Entries").
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            entries = (Map<String, Object>) snapshot.getValue();
                            try {
                                listOfDates = new ArrayList<String>(entries.keySet());
                            } catch (Exception e) {
                                System.out.println(listOfDates);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("TESTING4");
                        }

                    });
        } else {
            System.out.println("DATABSE NULL");
        }


        // Format the dates into the ListView
        if(listOfDates != null) {
            Collections.sort(listOfDates);
            for(int x = 0; x < listOfDates.size(); x++)
            {
                String tempDate = listOfDates.get(x);
                String tempFormattedDate = tempDate.substring(0,2) + "/" + tempDate.substring(2,4) + "/" + tempDate.substring(4,6);
                String tempPerfectedDate;
                String tempMostPerfectedDate;
                if(tempFormattedDate.charAt(0) == '0') {
                    tempPerfectedDate = tempFormattedDate.substring(1);
                }
                else{
                    tempPerfectedDate = tempFormattedDate;
                }
                if(tempFormattedDate.charAt(3) == '0') {
                    tempMostPerfectedDate = tempPerfectedDate.substring(0,1+tempPerfectedDate.indexOf("/"));
                    tempMostPerfectedDate += tempFormattedDate.substring(4);
                }
                else{
                    tempMostPerfectedDate = tempPerfectedDate.substring(0,1+ tempPerfectedDate.indexOf("/"));
                    tempMostPerfectedDate += tempFormattedDate.substring(3);
                }
                listOfDates.set(x,tempMostPerfectedDate);
            }
            ListAdapter historyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfDates);
            ListView historyListView = (ListView) getView().findViewById(R.id.historyList);
            historyListView.setAdapter(historyAdapter);
            historyListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String date = ((TextView)view).getText().toString();

                            String textDate;
                            String [] dates = date.split("/");
                            if(dates[0].length() < 2)
                            {
                                textDate = "0" + dates[0];
                            }
                            else{
                                textDate = dates[0];
                            }
                            if(dates[1].length() < 2)
                            {
                                textDate += "0" + dates[1];
                            }
                            else{
                                textDate += dates[1];
                            }

                            textDate += dates[2];
                            String text = entries.get(textDate).toString();
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                            myAlert.setMessage(text)
                                    .setPositiveButton("Finished!", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setTitle(date)
                                    .create();
                            myAlert.show();

                        }
                    }
            );
        }
    }
}
