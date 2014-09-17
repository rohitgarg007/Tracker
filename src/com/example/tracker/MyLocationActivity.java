package com.example.tracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.example.tracker.LocationSenderService.MyLocalBinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyLocationActivity extends Activity
{
    // Google Map
    private GoogleMap googleMap;
 SharedPreferences pref;
 TextView cur_points;
 Location location;
 Geocoder geo;
 Button start_stop;
 TextView total_runup,total_runup_cation;
 LocationSenderService locationservice;
 String mylocation="";
 Double Startlat,Startlong;
 Location curr_location,startlocation;
 TimerTask ttask;
 Timer t=new Timer();
 float bearing;
 int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starttourlayout);
        total_runup=(TextView)findViewById(R.id.textView_total_runup);
        total_runup_cation=(TextView)findViewById(R.id.textView_totalcaption);
        start_stop=(Button)findViewById(R.id.button_start_stop_tour);
        start_stop.setVisibility(View.GONE);
        total_runup.setVisibility(View.GONE);
        total_runup_cation.setVisibility(View.GONE);
        bindService(new Intent(this,LocationSenderService.class), myConnection, Context.BIND_AUTO_CREATE);
        pref = getSharedPreferences("Ids",Context.MODE_PRIVATE);   
        
        try {
            // Loading map
            initilizeMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            
            googleMap.setMyLocationEnabled(true);
            
            // Enable / Disable zooming controls
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.setBuildingsEnabled(true);
           
            googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() 
            {                
                @Override
                public void onMyLocationChange(Location location) {
                    // TODO Auto-generated method stub
                    if(location!=null)
                    {
                    curr_location=location;
//                    CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(googleMap.getCameraPosition().zoom)
//                    .build();
//                 mylocation=GetAddress(location.getLatitude(), location.getLongitude());
//            googleMap.animateCamera(CameraUpdateFactory
//                    .newCameraPosition(cameraPosition));
//            if(startlocation!=null)
//            total_runup.setText(String.format("%.2f", curr_location.distanceTo(startlocation)/1000)+" Km");
           // cur_points.setText(location.getLatitude()+"\n"+location.getLongitude());
                }
                }
            });    
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        start_stop.setOnClickListener(new OnClickListener()
        {            
            @Override 
            public void onClick(View arg0) 
            {
                // TODO Auto-generated method stub
                if(count==2)
                {
                   finish(); 
                }
                if(count==1)
                {                
                start_stop.setText("Close");
                mydialog("Total Fair","Your total fair is : \n"+String.format("%.2f",( curr_location.distanceTo(startlocation)/1000)*10)+" Rs");
                //finish();
                count++;
                }
                if(count==0)
                {
                start_stop.setText("Stop Tour");
                startlocation=curr_location;
                count++;
                }                
            }
        });
        ttask=new TimerTask()
        {                    
            @Override
            public void run() 
            {
                // TODO Auto-generated method stub
                if(curr_location!=null)
                {
                    if(startlocation!=null)
                    {
                        Log.e("Lat",""+curr_location.getLatitude());
                        Log.e("Long",""+curr_location.getLongitude());
                        Log.e("S_Lat",""+startlocation.getLatitude());
                        Log.e("S_Long",""+startlocation.getLongitude());
                        bearing=curr_location.bearingTo(startlocation);
                        runOnUiThread(new Runnable() 
                        {                           
                            @Override
                            public void run() 
                            {
                                // TODO Auto-generated method stub
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(curr_location.getLatitude(),curr_location.getLongitude())).zoom(googleMap.getCameraPosition().zoom)
                                .bearing(Math.round(bearing)).build();                            
                                googleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));
                            }
                        });
                        startlocation=curr_location;
                        pref.edit().putFloat("StartLocation_Lat",(float) startlocation.getLatitude()).commit();
                        pref.edit().putFloat("StartLocation_Long",(float) startlocation.getLongitude()).commit();
                    }
                    else
                    {
                        startlocation=curr_location;
                        Log.e("Last","Null");
                    }
                }
                else
                {
                    Log.e("Curr","Null");
                }
            }
        };
        t.scheduleAtFixedRate(ttask, 0, 1000);
    }
    
    public String GetAddress(Double lat, Double lon)
    {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        String ret = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
              
                strReturnedAddress.append(addresses.get(0).getCountryName()).append("\n");
                ret = strReturnedAddress.toString();
            }
            else{
                ret = "No Address returned!";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ret = "Can't get Address!";
        }
        return ret;
    }
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() 
    {
        if (googleMap == null)
        {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();           
            // check if map is created successfully or not
            if (googleMap == null) 
            {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.first, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
     
        case R.id.mylocation:
            if(curr_location!=null)
            mydialog("My Location",GetAddress(curr_location.getLatitude(), curr_location.getLongitude()));
        default:
            return super.onOptionsItemSelected(item);
        }
    }   
    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                IBinder service) {
            MyLocalBinder binder = (MyLocalBinder) service;
            Log.d("Service","Bound");
            locationservice = binder.getService();
        }
        
        public void onServiceDisconnected(ComponentName arg0) 
        {
            Log.d("Service","unbound");
        }
        
       };
       @SuppressWarnings("deprecation")
       public void mydialog(String Title,String msg) 
       {
           final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
           alertDialog.setTitle(Title);
           alertDialog.setMessage(msg);
           alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {

                  //here you can add functions
      alertDialog.dismiss();
               } });
           alertDialog.setIcon(R.drawable.ic_launcher);
           alertDialog.show();
       }    
    }
