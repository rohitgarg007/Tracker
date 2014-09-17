package com.example.tracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.adapters.CompletedTourAdapter;
import com.example.classes.BookingData;
import com.example.service.GetBookingService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CompletedTourActivity extends Activity
{
ListView list;
SharedPreferences pref;
ProgressDialog pd;
ArrayList<BookingData> data;
CompletedTourAdapter adapter;
LinearLayout root;
TextView ClientName,SourceAdd,SourceTime,DesAdd,DesTime,Remarks,Contact,landmark,pow;
Button close,starttour;
    @Override
    public void onCreate(Bundle saved)
    {
        super.onCreate(saved);
        setContentView(R.layout.completedtourlayout);
        pref = getSharedPreferences("Ids",Context.MODE_PRIVATE);
        list=(ListView)findViewById(R.id.listView_completedtour); 
        pow=(TextView)findViewById(R.id.textView_poweredby);
        new GetBookingTask().execute("");
        pow.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        onPoweredBy();
                    }
                });
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
            {
                // TODO Auto-generated method stub
               
                    dialog(arg2);
                
            }
        });
    }
    class GetBookingTask extends AsyncTask<String, String, String>
    {
        GetBookingService g;
        @Override
        public void onPreExecute()
        {
          //pd=ProgressDialog.show(BookingActivity.this, null, "Getting booking...");  
          //data=new ArrayList<BookingData>();            
            setProgressBarIndeterminateVisibility(true);
            Toast.makeText(CompletedTourActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            g=new GetBookingService();            
            return g.Call(pref.getString("DriverId", ""));
        }
        @Override
        public void onPostExecute(String result)
        {
            if(!result.equals("Error"))
            {
                if(!result.equals("[]"))
                {
                    Log.d("Result", result);
                   convert(result); 
                   if(!data.isEmpty())
                   {
                   adapter=new CompletedTourAdapter(CompletedTourActivity.this, data);
                   list.setAdapter(adapter);
                   //pd.cancel();
                   }
                   else
                   {
                       mydialog("No completed tour yet...");
                   }
                }
                else
                {
                    mydialog("No booking for the day.");
                }
            }
            else
            {
                Toast.makeText(CompletedTourActivity.this,result,Toast.LENGTH_LONG).show();
                //pd.cancel();
                setProgressBarIndeterminateVisibility(false);
                mydialog("Network error occur please retry.");
            }
        }
    }
    public void convert(String Json)
    {
        try
        {
        JSONArray array=new JSONArray(Json);
            if(array.length()>0)
            {
                data=new ArrayList<BookingData>();
                for(int i=0;i<array.length();i++)
                {
                 JSONObject object=array.getJSONObject(i);
                 Log.d("Client Name",  object.getString("ClientName"));
                 if(object.getString("Status").equals("Close")||object.getString("Status").equals("Dispatch")&&!object.getString("Id").equals(pref.getString("CurrentTourId","")))
                 {
                 data.add(new BookingData(object.getString("Id"), object.getString("ClientName"), object.getString("Contact1"),
                         object.getString("Contact2"), object.getString("MailId"), object.getString("SourcePoint"),
                         object.getString("Sourcedate"), object.getString("SourceTime"), object.getString("DestinationPoint"),
                         object.getString("DestinationDate"), object.getString("DestinationTime"), object.getString("Landmark")
                         ,object.getString("Remark"),object.getString("Status"),
                         object.getString("Rate").trim().equals("0")?object.getString("FixedRate").trim():object.getString("Rate").trim()
                         ,object.getString("Rate").trim().equals("0")?"Fixed Rate":"Rate"));
                 }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    @SuppressWarnings("deprecation")
    public void mydialog(String msg) 
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Warming");
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {

               //here you can add functions
    CompletedTourActivity.this.finish();
            } });
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }
    public void dialog(final int pos)
    {
        final Dialog d=new Dialog(this);
        d.setTitle("Booking Details");
        d.setContentView(R.layout.dialoglayout);
        landmark=(TextView)d.findViewById(R.id.textView_d_landmark);
        close=(Button)d.findViewById(R.id.Button_d_Close);
        starttour=(Button)d.findViewById(R.id.Button_d_tour); 
        starttour.setVisibility(View.GONE);
        ClientName=(TextView)d.findViewById(R.id.textView_d_ClientName);
        Contact=(TextView)d.findViewById(R.id.textView_d_contact);
        DesAdd=(TextView)d.findViewById(R.id.textView_d_DesitnationAdd);
        DesTime=(TextView)d.findViewById(R.id.textView_d_DestinationTime);
        SourceAdd=(TextView)d.findViewById(R.id.textView_d_SourceAdd);
        SourceTime=(TextView)d.findViewById(R.id.textView_d_sourceTime);
        Remarks=(TextView)d.findViewById(R.id.textView_d_Remarks);
        ClientName.setText(data.get(pos).getClientName());
        Contact.setText(data.get(pos).Contact1());
        DesAdd.setText(data.get(pos).DestinationPoint());
        DesTime.setText(data.get(pos).DestinationDate()+" At "+data.get(pos).DestinationTime());
        SourceAdd.setText(data.get(pos).SourcePoint());
        SourceTime.setText(data.get(pos).getSourcedate()+" At "+data.get(pos).getSourceTime());
        Remarks.setText(data.get(pos).Remarks());
        
        if(data.get(pos).Landmark().equals(""))
        landmark.setText("");
        else
            landmark.setText(data.get(pos).Landmark());    
        close.setOnClickListener(new OnClickListener()
        {            
            @Override
            public void onClick(View arg0) 
            {
                // TODO Auto-generated method stub
            d.dismiss();    
            }
        });
        starttour.setOnClickListener(new OnClickListener()
        {          
            @Override
            public void onClick(View v) 
            {
                // TODO Auto-generated method stub
               Intent i=new Intent(CompletedTourActivity.this,StartTourActivity.class);
               i.putExtra("Id", data.get(pos).Id());
               i.putExtra("ClientName", data.get(pos).getClientName());
               i.putExtra("ContactNo", data.get(pos).Contact1());
               i.putExtra("SourceAdd", data.get(pos).SourcePoint());
               i.putExtra("SourceDate", data.get(pos).getSourcedate());
               i.putExtra("SourceTime", data.get(pos).getSourceTime());
               i.putExtra("DesAdd",data.get(pos).DestinationPoint());
               i.putExtra("DesDate", data.get(pos).DestinationDate());
               i.putExtra("DesTime", data.get(pos).DestinationTime());
               i.putExtra("LandMark", data.get(pos).Landmark());
               i.putExtra("Status", data.get(pos).Status());
               i.putExtra("Remark", data.get(pos).Remarks());
               i.putExtra("Price", data.get(pos).Price());
               i.putExtra("Type", data.get(pos).Type());
               startActivity(i);
               d.dismiss();
            }
        });
        d.show();
    }
    @Override
    public void onResume()
    {
        super.onResume();
//        if(pref.getBoolean("Refreshing", false))
//        {
        new GetBookingTask().execute("");
//        pref.edit().putBoolean("Refreshing", false).commit();
//        }
//        new Thread(new Runnable() {
//            
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//           GetBookingService g=new GetBookingService();
//           String response= g.Call(pref.getString("DriverId", ""));
//           if(!response.equals("Error"))
//           {
//               if(!response.equals("[]"))
//               {
//                   Log.d("Result", response);                   
//                  convert(response); 
//                  runOnUiThread(new Runnable() {
//                    
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        adapter=new BookingAdapter(BookingActivity.this, data);
//                        list.setAdapter(adapter);
//                    }
//                });
//                                   
//               }               
//           }
//           else
//           {
//           }
//            }
//        }).start();
    }
    public void onPoweredBy()
    {
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                  Uri.parse("http://www.prevoirinfotech.com"));
                startActivity(viewIntent);
    }
}
