package com.example.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class AutoStart extends BroadcastReceiver {
SharedPreferences pref;
    @Override
    public void onReceive(Context arg0, Intent arg1)
    {
        // TODO Auto-generated method stub
        pref = arg0.getSharedPreferences("Ids",Context.MODE_PRIVATE);
        if(!pref.getString("CabId", "").equals(""))
        {
        Intent intent = new Intent(arg0,LocationSenderService.class);
        intent.putExtra("CabId",pref.getString("CabId", ""));
        intent.putExtra("DriverId",pref.getString("DriverId", ""));
        arg0.startService(intent);
        Toast.makeText(arg0, "Service Auto Start", Toast.LENGTH_SHORT).show();
        }
    }

}
