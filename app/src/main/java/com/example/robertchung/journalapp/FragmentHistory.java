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

        System.out.println(userUID);
        if (mDatabase != null) {
            mDatabase.child("Users").child(userUID).child("Entries").
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            System.out.println("TESTING3");
                            entries = (Map<String, Object>) snapshot.getValue();
                            listOfDates = new ArrayList<String>(entries.keySet());
                            System.out.println("TESTING2");
                            System.out.println(listOfDates);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("TESTING4");
                        }

                    });
        } else {
            System.out.println("DATABSE NULL");
        }

//        String[] test = {"2/20/18","2/21/18","2/22/18","2/23/18","2/24/18","2/25/18","2/26/18","2/27/18"};
        System.out.println("TESTING");
        System.out.println(listOfDates);

        // TODO CONNOR BELOW
        // listOfDates is list of dates
        // you can get entry by doing a entries.get(<INSERT DATE HERE>) ** HAS TO BE CORRECT FORMATTED DATE
        // For above, might have to redo formatting so that it's consistent like 030418

        if(listOfDates != null) {
            ListAdapter historyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfDates);
            ListView historyListView = (ListView) getView().findViewById(R.id.historyList);
            historyListView.setAdapter(historyAdapter);
            historyListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String date = ((TextView)view).getText().toString();
                            String text = entries.get(date).toString();
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
