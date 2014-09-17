package com.example.tracker;

import java.util.Timer;
import java.util.TimerTask;

import com.example.service.GetBookingNotificationService;
import com.example.service.LocationUpdaterService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class LocationSenderService extends Service {
public static Double lat=0.0,longi=0.0;
public static Double lastlat=0.0,lastlongi=0.0;
Timer mTimer=new Timer();
String CabId,DriverId,NotiData;
SharedPreferences pref;
String max="";
int i;
	public LocationSenderService() {
		// TODO Auto-generated constructor stub
	}
	private final IBinder myBinder = new MyLocalBinder();
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return myBinder;
    }
    public class MyLocalBinder extends Binder 
    {
        LocationSenderService getService() {
            return LocationSenderService.this;
        }
    }

	@Override
	public void onCreate()
	{
		
	}
	 @Override
	  public int onStartCommand(final Intent intent, int flags, int startId)
	   {
		Log.e("StaRT Id", ""+startId);
		pref = getSharedPreferences("Ids",Context.MODE_PRIVATE);
		 //if(startId == 1)
		 {
		     LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		        MyLocationListener mlocListener = new MyLocationListener();
		        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,
		                    0, mlocListener);   
		        final LocationUpdaterService l=new LocationUpdaterService();
		        mTimer.scheduleAtFixedRate(new TimerTask() {
		            @Override
		            public void run() {
		                // TODO Auto-generated method stub  
		                if(lat!=0.0&&longi!=0.0)
		                if(lat!=lastlat||longi!=lastlongi)
		                {                                   
		                String res=l.Call(String.valueOf(lat),String.valueOf(longi),pref.getString("CabId", ""),pref.getString("DriverId", ""));
		                Log.e("CabId", pref.getString("CabId", ""));
		                Log.e("DriverId",pref.getString("DriverId", ""));
		                lastlat=lat;
		                lastlongi=longi;
		                Log.d("Result",res);  
		                
		                //Toast.makeText( LocationSenderService.this,"Position Added", Toast.LENGTH_SHORT).show();       
		                }
		                else
		                {
		                    Log.d("Else","Else");   
		                }
		              
		                GetBookingNotificationService g=new GetBookingNotificationService();
		                NotiData=g.Call(pref.getString("DriverId", ""));
		                if(!NotiData.equals("[]")&&!NotiData.equals("Error")&&!NotiData.equals(max))
		                {
		                    max=NotiData;
		                    notification();
		                }
		            }
		        }, 0,60000);
		 }
	    return (START_STICKY);
	   }
	public void  Timer(final Intent intent) 
	{
	   	
	}
	 public class MyLocationListener implements LocationListener
	 {
	 int f=1;
	 public void onLocationChanged(Location loc) {
	     // TODO Auto-generated method stub
	    //if(f==1)
	    {
	     lat=loc.getLatitude();
	     longi=loc.getLongitude();
	    }

	     String Text = "My current location is: " +
	     "Latitude = " + lat +
	     "Longitude = " +longi;
	    //Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show();

	 }
	@Override
	public void onProviderDisabled(String arg0) 
	{
		// TODO Auto-generated method stub
		 Toast.makeText( getApplicationContext(), "Service Disabled", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onProviderEnabled(String provider) 
	{
		// TODO Auto-generated method stub
		 Toast.makeText( getApplicationContext(), "Service Enabled", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub
		
	}
	}
	 public static Double getLat() 
	 {
		return lat;
	}
	 public static Double getLong() 
	 {
	return longi;	
	}
	 public void notification()
     {
         Intent intent = new Intent(this,BookingActivity.class);
         PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
         NotificationCompat.Builder mBuilder =
                  new NotificationCompat.Builder(this)
                  .setSmallIcon(R.drawable.ic_launcher)
                  .setContentTitle("Jaipur Cab Service")
                  .setContentText("You have new booking tap to view.")
                  .setContentIntent(pIntent); 
         mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
         mBuilder.setAutoCancel(true);
         NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         notificationManager.notify(0, mBuilder.build());
     }
	public class GetNotification extends AsyncTask<String, String, String>
	{
	    GetBookingNotificationService g;
	    @Override
	    public void onPreExecute()
	    {
	        
	    }
        @Override
        protected String doInBackground(String... params) 
        {
            // TODO Auto-generated method stub
            g=new GetBookingNotificationService();
            return g.Call(pref.getString("DriverId",""));
        }
	    @Override
	    public void onPostExecute(String result)
	    {
	        if(!result.equals("[]")&&!result.equals("Error"))
	        {
	            notification();
	        }
	    }
	}
}
