package com.example.tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends Activity {
Button booking,my_location,logout;
SharedPreferences pref;
AutoStart reciever;
TextView pow;
int count=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		pref = getSharedPreferences("Ids",Context.MODE_PRIVATE);		
		if(!pref.getBoolean("LoggedIn",false))
        {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        else
        {
            new Thread(new Runnable() {
                
                @Override
                public void run() {
//                     TODO Auto-generated method stub
                    Intent i=new Intent(getApplicationContext(),LocationSenderService.class);
                    Log.e("CabId", pref.getString("CabId", ""));
                    Log.e("DriverId",pref.getString("DriverId", ""));
                    i.putExtra("CabId",pref.getString("CabId", ""));
                    i.putExtra("DriverId",pref.getString("DriverId", ""));
                    startService(i);       
                }
            }).start();    
        }		
		setContentView(R.layout.activity_first);
		pow=(TextView)findViewById(R.id.textView_poweredby);
		booking=(Button)findViewById(R.id.button_bookings);
		my_location=(Button)findViewById(R.id.button_curr_loc);		
		logout=(Button)findViewById(R.id.button_logout);
		logout.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pref.edit().clear().commit();
                startActivity(new Intent(FirstActivity.this,LoginActivity.class));
                finish(); 
            }
        });
        pow.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        onPoweredBy();
                    }
                });
		booking.setOnClickListener(new OnClickListener() 
		{           
            @Override
            public void onClick(View arg0) 
            {
                // TODO Auto-generated method stub
               startActivity(new Intent(FirstActivity.this,BookingActivity.class)); 
            }
        });
		my_location.setOnClickListener(new OnClickListener() 
		{            
            @Override
            public void onClick(View v) 
            {
                // TODO Auto-generated method stub
                startActivity(new Intent(FirstActivity.this,MyLocationActivity.class));
            }
        });
		}
public void onCompletedTour(View view)
{
    Intent i=new Intent(this,CompletedTourActivity.class);
    startActivity(i);
}
public void onPoweredBy()
{
    Intent viewIntent =
            new Intent("android.intent.action.VIEW",
              Uri.parse("http://www.prevoirinfotech.com"));
            startActivity(viewIntent);
}
}
