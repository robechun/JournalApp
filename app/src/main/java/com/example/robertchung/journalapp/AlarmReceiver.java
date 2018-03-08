package com.example.robertchung.journalapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Forche on 3/8/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       	/*Intent service1 = new Intent(context, MyAlarmService.class);
	     context.startService(service1);*/
        Log.i("App", "called receiver method");
        try{
            Utils.generateNotification(context);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
