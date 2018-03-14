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

public class FragmentHistory extends Fragment {
    private final int REQUEST_TEXT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] test = {"2/20/18","2/21/18","2/22/18","2/23/18","2/24/18","2/25/18","2/26/18","2/27/18"};
        ListAdapter historyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, test);
        ListView historyListView = (ListView) getView().findViewById(R.id.historyList);
        historyListView.setAdapter(historyAdapter);

        historyListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //this gives you the string value of the item you clicked.
                        String date = ((TextView)view).getText().toString();
                        //TODO: use this information to get the entry
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                        myAlert.setMessage("Place data from database here.")
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
