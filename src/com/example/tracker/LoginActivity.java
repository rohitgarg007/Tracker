package com.example.tracker;

import com.example.service.CheckUserService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity
{
    EditText cab_no,password;
    Button login;
    SharedPreferences pref;
    ProgressDialog pd;
    TextView pow;
    @Override
    public void onCreate(Bundle Saved)
    {
        super.onCreate(Saved);
        pref = getSharedPreferences("Ids",Context.MODE_PRIVATE);
        setContentView(R.layout.loginlayout);
        cab_no=(EditText)findViewById(R.id.editText_cab_no);
        password=(EditText)findViewById(R.id.editText_login_password);
        login=(Button)findViewById(R.id.button_login);
        pow=(TextView)findViewById(R.id.textView_poweredby);
        pow.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onPoweredBy();
            }
        });
        login.setOnClickListener(new OnClickListener() 
        {           
            @Override
            public void onClick(View arg0)
            {
                // TODO Auto-generated method stub
                if(!cab_no.getText().toString().isEmpty()&&!password.getText().toString().isEmpty())
                {
                   new CheckLogin().execute(cab_no.getText().toString().trim(),password.getText().toString().trim()); 
                }
            }
        });
    }
class CheckLogin extends AsyncTask<String, String, String>
{
CheckUserService c;
    @Override
    public void onPreExecute()
    {
       pd=ProgressDialog.show(LoginActivity.this,null,"Process your data....."); 
    }
    @Override
    protected String doInBackground(String... arg0) 
    {
        // TODO Auto-generated method stub
        c=new CheckUserService();
        return c.Call(arg0[0], arg0[1]);
    }
    @Override
    public void onPostExecute(String result)
    {
        pd.cancel();
        if(result.equals("Error"))
        {
         Toast.makeText(LoginActivity.this,result, Toast.LENGTH_SHORT).show();   
        }
        if(!result.equals("false"))
        {
            Log.e("output", result);    
            AutoStart  reciever=new AutoStart();
            registerReceiver(reciever,new IntentFilter("android.intent.action.BOOT_COMPLETED"));
            pref.edit().putBoolean("LoggedIn", true).putString("CabId", result.substring(0,result.indexOf(",")))
            .putString("DriverId", result.substring(result.lastIndexOf(",")+1))
            .putString("DriverName", result.substring(result.indexOf(",")+1,result.lastIndexOf(",")))
            .putString("CabNo", cab_no.getText().toString().trim()).commit();
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Intent i=new Intent(getApplicationContext(),LocationSenderService.class);
                    Log.e("CabId", pref.getString("CabId", ""));
                    Log.e("DriverId",pref.getString("DriverId", ""));
                    i.putExtra("CabId",pref.getString("CabId", ""));
                    i.putExtra("DriverId",pref.getString("DriverId", ""));
                    startService(i);       
                }
            }).start();                     
            Intent i1=new Intent(LoginActivity.this,FirstActivity.class);
            startActivity(i1);
            finish();
        }
        else
        {
            Toast.makeText(LoginActivity.this,"Enter the correct user name and password", Toast.LENGTH_SHORT).show(); 
        }
    }
}
public void onGoToWebsite(View view)
{
    Intent viewIntent =
            new Intent("android.intent.action.VIEW",
              Uri.parse("http://www.jaipurcabservice.com/"));
            startActivity(viewIntent);
}
public void onContact(View view)
{
    Intent viewIntent =
            new Intent("android.intent.action.VIEW",
              Uri.parse("http://www.jaipurcabservice.com/CostumerCare.aspx"));
            startActivity(viewIntent);
}
public void onPoweredBy()
{
    Intent viewIntent =
            new Intent("android.intent.action.VIEW",
              Uri.parse("http://www.prevoirinfotech.com"));
            startActivity(viewIntent);
}
}
