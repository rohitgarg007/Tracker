package com.example.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.classes.BookingData;
import com.example.tracker.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookingAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    String[] rank;
    ArrayList<String> time,org_name,date;
    ArrayList<String> name;
    ArrayList<Integer> flag;
    LayoutInflater inflater;
    ArrayList<BookingData> data;
    SharedPreferences pref;
    public BookingAdapter(Context context,ArrayList<BookingData> data)
    {
        this.context = context;
        this.data=data;
        pref = context.getSharedPreferences("Ids",Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        viewHolder holder;
        if(convertView==null){
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder=new viewHolder();
        convertView = inflater.inflate(R.layout.bookinglistviewitem, parent, false);
        // Locate the TextViews in listview_item.xml
        holder.ClinetName = (TextView) convertView.findViewById(R.id.textView_bookinglistitem_ClientName);
        holder.time=(TextView)convertView.findViewById(R.id.textView_bookinglistitem_Time); 
        holder.root=(LinearLayout)convertView.findViewById(R.id.bookingitemlistviewroot);
        holder.status=(TextView)convertView.findViewById(R.id.textView_bookinglistitem_status);
        holder.Contact=(TextView)convertView.findViewById(R.id.textView_bookinglistitem_Clientphoneno);
        convertView.setTag(holder);
        }
        else
        {
            holder=(viewHolder)convertView.getTag();
        }
        // Capture position and set to the TextViews
        if(data.get(position).Status().equals("Dispatch")&&pref.getFloat("StartLocation_Lat",0)!=0&&data.get(position).Id().equals(pref.getString("CurrentTourId","")))
        {
            holder.ClinetName.setText(data.get(position).getClientName());
            //holder.root.setBackgroundColor(Color.RED);
            holder.status.setText("Tour is going on.");
        }
//        else if(data.get(position).Status().equals("Dispatch"))
//        {
//            holder.ClinetName.setText(data.get(position).getClientName());
//            //holder.root.setBackgroundColor(Color.GREEN);
//            holder.status.setText("Tour completed but not close by Administrator");
//        }
        if(data.get(position).Status().equals("Allot"))
        {
        holder.ClinetName.setText(data.get(position).getClientName());
        //holder.root.setBackgroundColor(Color.MAGENTA);
        holder.status.setText("New Tour");
        }
        holder.time.setText(data.get(position).getSourcedate()+" "+data.get(position).getSourceTime());
        holder.Contact.setText(data.get(position).Contact1());
        return convertView;
}
    public class viewHolder{
        TextView ClinetName;
        TextView time,status,Contact;
        LinearLayout root;
    }
}