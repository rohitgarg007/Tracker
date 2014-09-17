package com.example.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classes.SendMailClass;
import com.example.service.UpdateAmountService;
import com.example.service.UpdateDispatchService;
import com.example.tracker.LocationSenderService.MyLocalBinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class StartTourActivity extends Activity
{
    // Google Map
    private GoogleMap googleMap;
 SharedPreferences pref;
 TextView cur_points;
 Location location;
 Geocoder geo;
 Button start_stop;
 TextView total_runup;
 LocationSenderService locationservice;
 String mylocation="";
 Double Startlat,Startlong;
 Location curr_location,startlocation=null;
 String Source_Add,Destination_Add,Con_No;
 protected PowerManager.WakeLock mWakeLock;
 String Id,ClientName,ContactNo,SourceAdd,SourceDate,SourceTime,DesAdd,DesDate,DesTime,Status,LandMark,Remark,Type;
 int Price;
 Intent i;
 int count=0;
 float totalRunUp=0;
 Timer updater=new Timer();
 TimerTask ttask;
 ArrayList<Location> myRoadWay;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starttourlayout);
        myRoadWay=new ArrayList<Location>();
        i=getIntent();        
        Id=i.getStringExtra("Id");
        Type=i.getStringExtra("Type");
        Price=Integer.parseInt(i.getStringExtra("Price"));
        ClientName=i.getStringExtra("ClientName");
        ContactNo=i.getStringExtra("ContactNo");
        SourceAdd=i.getStringExtra("SourceAdd");
        SourceDate=i.getStringExtra("SourceDate");
        SourceTime=i.getStringExtra("SourceTime");
        DesAdd=i.getStringExtra("DesAdd");
        DesDate=i.getStringExtra("DesDate");
        DesTime=i.getStringExtra("DesTime");
        LandMark=i.getStringExtra("LandMark");
        Status="Dispatch";
        Remark=i.getStringExtra("Remark");
        if(Remark.equals("null"))
        {
            Remark="";
        }
        total_runup=(TextView)findViewById(R.id.textView_total_runup);
        start_stop=(Button)findViewById(R.id.button_start_stop_tour);
        bindService(new Intent(this,LocationSenderService.class), myConnection, Context.BIND_AUTO_CREATE);
        pref = getSharedPreferences("Ids",Context.MODE_PRIVATE);   
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.e("e1",""+(pref.getFloat("StartLocation_Lat",0)));
        Log.e("e2",""+(pref.getFloat("StartLocation_Long",0)));
        if(pref.getFloat("StartLocation_Lat",0)!=0 && pref.getFloat("StartLocation_Long",0)!=0)
        { 
            startlocation=new Location("");
            startlocation.setLatitude(pref.getFloat("StartLocation_Lat",0));
            startlocation.setLongitude(pref.getFloat("StartLocation_Long",0));
            count=1;
            start_stop.setText("Stop Tour");
        }
        myRoadWay=new ArrayList<Location>();
//        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
       // this.mWakeLock.acquire();
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
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(googleMap.getCameraPosition().zoom)
                    .build();
                 mylocation=GetAddress(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
           // cur_points.setText(location.getLatitude()+"\n"+location.getLongitude());
                }
                }
            });    
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
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
                        totalRunUp+=curr_location.distanceTo(startlocation)/1000;
                        runOnUiThread(new Runnable() 
                        {                           
                            @Override
                            public void run() 
                            {
                                // TODO Auto-generated method stub
                                total_runup.setText(String.format("%.2f", totalRunUp)+" Km");    
                            }
                        });
                        startlocation=curr_location;
                        pref.edit().putFloat("StartLocation_Lat",(float) startlocation.getLatitude()).commit();
                        pref.edit().putFloat("StartLocation_Long",(float) startlocation.getLongitude()).commit();
                    }                    
                }                
            }
        };
        new Timer().scheduleAtFixedRate(ttask, 0, 6000);
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
                    ttask.cancel();
                    updater.cancel();
                    start_stop.setText("Close");
                    if(Type.equals("Rate"))
                    mydialog1("Total Fair","Your total fair is : \n"+String.format("%.2f",(totalRunUp*Price))+" Rs"+"\n and your total run up is: "+totalRunUp);
                    else
                    mydialog1("Total Fair","Your total fair is : \n"+Price+" Rs"+"\n and your total run up is: "+totalRunUp);    
                //finish();
                count++;
                pref.edit().putBoolean("Refreshing",true).commit();
                }
                if(count==0&&curr_location!=null)
                {
                start_stop.setText("Stop Tour");
                startlocation=curr_location;
                pref.edit().putString("CurrentTourId",Id).commit();
                count++;
                final SendMailClass mail=new SendMailClass();
                new Thread(new Runnable() {
                    
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mail.SendingMail("aman.singhal1804@gmail.com","aman.singhal1804@gmail.com","Jaipur Cab","Hello Sir,\n the cab has been dispatched to run with the following information "
                                + "\n Driver Name:"+pref.getString("DriverName","Not Set")
                                +"\n Cab No:"+pref.getString("CabNo","Not Set")
                                +"\n Client Name:"+ClientName
                                +"\n Status:"+Status
                                +"\n Source Point:"+ SourceAdd
                                +"\n Source Time"+SourceDate+"At "+SourceTime
                                +"\n Destination Address:"+DesAdd
                                +"\n Destination Time:"+DesDate+"At "+DesTime
                                +"\n LandMark:"+LandMark
                                +"\n Remark:"+Remark
                                +"\n "+Type+":"+Price);  
                        UpdateDispatchService u=new UpdateDispatchService();
                        Log.d("Dispatch Update",u.Call(Id));
                    }
                }).start();              
                pref.edit().putBoolean("Refreshing",true).commit();               
                }
                if(curr_location==null)
                {
                    Toast.makeText(StartTourActivity.this,"Location Not Found",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
 
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
        } catch (Exception e) {
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
            mydialog("My Location",mylocation);
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
        
        public void onServiceDisconnected(ComponentName arg0) {
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
       @Override
       public boolean onKeyDown(int keyCode, KeyEvent event)  {
           if (keyCode == KeyEvent.KEYCODE_BACK) 
           {
               Log.d("CDA", "onKeyDown Called");
               //moveTaskToBack(true);
               ///this.finish();
             return true;
           }

           return super.onKeyDown(keyCode, event);
       }
       public void mydialog1(String Title,String msg) 
       {
           final Dialog alertDialog = new Dialog(this);           
           alertDialog.setContentView(R.layout.dialogamountlayout);
           alertDialog.setTitle(Title);
           alertDialog.setCanceledOnTouchOutside(false);
           TextView msgbox=(TextView)alertDialog.findViewById(R.id.textView_dia_amount_msg);
           msgbox.setText(msg);
           final EditText rs=(EditText)alertDialog.findViewById(R.id.editText_d_amount_rs);
           Button ok=(Button)alertDialog.findViewById(R.id.button_dia_amount_ok);
           ok.setOnClickListener(new OnClickListener() {              
            @Override
            public void onClick(View arg0)
            {
                // TODO Auto-generated method stub
                //here you can add functions
                
                final SendMailClass mail=new SendMailClass();
                new Thread(new Runnable() {
                    
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mail.SendingMail("aman.singhal1804@gmail.com","aman.singhal1804@gmail.com","Jaipur Cab","Hello Sir,\n the cab has completed the tour."
                                +"\n Details:"
                                + "\n Driver Name:"+pref.getString("DriverName","Not Set")
                                +"\n Cab No:"+pref.getString("CabNo","Not Set")
                                +"\n Client Name:"+ClientName
                                +"\n Status:"+Status
                                +"\n Source Point:"+ SourceAdd
                                +"\n Source Time"+SourceDate+"At "+SourceTime
                                +"\n Destination Address:"+DesAdd
                                +"\n Destination Time:"+DesDate+"At "+DesTime
                                +"\n LandMark:"+LandMark
                                +"\n Remark:"+Remark
                                +"\n Total Run-Up:"+totalRunUp+" Km"
                                +"\n "+Type+":"+Price
                                +"\n Advance Amount:"+rs.getText().toString()); 
                        UpdateAmountService u=new UpdateAmountService();
                        Log.d("Amount Update",u.Call(Id, rs.getText().toString().trim()));
                    }
                    
                    
                }).start();

                pref.edit().putFloat("StartLocation_Lat",0).commit();
                pref.edit().putFloat("StartLocation_Long",0).commit();
                pref.edit().putBoolean("Refreshing",true).commit();
                pref.edit().putString("CurrentTourId","").commit();
                alertDialog.dismiss();
                finish();
            } });
           alertDialog.show();
       }    
     
}
