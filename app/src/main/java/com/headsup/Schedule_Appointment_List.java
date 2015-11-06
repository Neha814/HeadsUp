package com.headsup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Functions.Constants;
import utils.StringUtils;

/**
 * Created by sandeep on 4/11/15.
 */
public class Schedule_Appointment_List extends Activity implements View.OnClickListener{

    TextView calendar;
    MyAdapter mAdapter ;
    ListView listview;
    TextView date_to_show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sch_aapt_list);

        inIt();
    }

    private void inIt() {
        calendar = (TextView) findViewById(R.id.calendar);
        listview = (ListView) findViewById(R.id.listview);
        date_to_show = (TextView) findViewById(R.id.date_to_show);

        calendar.setOnClickListener(this);

        date_to_show.setText(Constants.DATE_TO_SHOW);

        if(Constants.apptList.size()>0) {

            mAdapter = new MyAdapter(getApplicationContext(),
                    Constants.apptList);
            listview.setAdapter(mAdapter);
        }else {
            StringUtils.showDialog("No data found.", Schedule_Appointment_List.this);
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constants.BOOKED_TIME_SLOT =Constants.apptList.get(i).get("keyname");
                Intent intent = new Intent(Schedule_Appointment_List.this , Appointment_detail.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==calendar){
            finish();
        }
    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater mInflater = null;

        public MyAdapter(Context context,
                         ArrayList<HashMap<String, String>> categoryList) {
            mInflater = LayoutInflater.from(getApplicationContext());

        }


        @Override
        public int getCount() {

            return Constants.apptList.size();
        }

        @Override
        public Object getItem(int position) {
            return Constants.apptList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.appt_listitem, null);

                holder.time_slot = (TextView) convertView.findViewById(R.id.time_slot);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.time_slot.setText(Constants.apptList.get(position).get("keyname"));

            return convertView;
        }

        class ViewHolder {
            TextView time_slot;
        }

    }
}