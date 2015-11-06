package com.headsup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Functions.Constants;
import Functions.Functions;
import robotocalendar.RobotoCalendarView;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 3/11/15.
 */
public class Schedule_Appointment_Calendar extends Activity implements View.OnClickListener , RobotoCalendarView.RobotoCalendarListener {

    TextView list;
    RobotoCalendarView robotoCalendarView;
    private Calendar currentCalendar;
    TextView dateToShow;
    ListView listview;
    boolean isConnected ;
    TransparentProgressDialog db;
    MyAdapter mAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sch_aapt_calendar);

        inIt();
    }

    private void inIt() {
        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());


        list = (TextView) findViewById(R.id.list);
        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);
        dateToShow = (TextView) findViewById(R.id.dateToShow);
        listview = (ListView) findViewById(R.id.listview);

        list.setOnClickListener(this);
        robotoCalendarView.setRobotoCalendarListener(this);

        currentCalendar = Calendar.getInstance(Locale.getDefault());
        robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());



        long date = System.currentTimeMillis();

       // SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");

        SimpleDateFormat sdf_tosend = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_tosend = sdf_tosend.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM  dd");
        String dateString = sdf.format(date);

        Constants.DATE_TO_SEND = dateString_tosend;

        dateToShow.setText(dateString);
        Constants.DATE_TO_SHOW = dateString;

        if (isConnected) {
            new apptlist().execute(new Void[0]);
        } else {
            StringUtils.showDialog(Constants.No_INTERNET, getApplicationContext());
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constants.BOOKED_TIME_SLOT =Constants.apptList.get(i).get("keyname");
                Intent intent = new Intent(Schedule_Appointment_Calendar.this , Appointment_detail.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSelected(Date date) {

        int d = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int m = currentCalendar.get(Calendar.MONTH);
        int y = currentCalendar.get(Calendar.YEAR);

     /*   System.out.println("date==" + date);

        System.out.println("d==" + d);
        System.out.println("m==" + m);
        System.out.println("y==" + y);

        String sdate = String.valueOf(d);
        String smonth = String.valueOf(m + 1);
        String syear = String.valueOf(y);

        System.out.println("sdate==" +sdate);
        System.out.println("smonth==" + smonth);
        System.out.println("syear==" + syear);*/

        robotoCalendarView.markDayAsSelectedDay(date);

        SimpleDateFormat sdf_tosend = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_tosend = sdf_tosend.format(date);
        System.out.println("**dateString_tosend**" + dateString_tosend);

        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM  dd");
        String dateString = sdf.format(date);

        dateToShow.setText(dateString);
        Constants.DATE_TO_SHOW = dateString;

        Constants.DATE_TO_SEND = dateString_tosend;

        if (isConnected) {
            new apptlist().execute(new Void[0]);
        } else {
            StringUtils.showDialog(Constants.No_INTERNET, getApplicationContext());
        }

    }

    @Override
    public void onRightButtonClick() {
       Constants.currentMonthIndex++;
        Constants.currentMonth++;
        updateCalendar();
    }

    @Override
    public void onLeftButtonClick() {
        Constants.currentMonthIndex--;
        Constants.currentMonth--;
        updateCalendar();
    }

    public void updateCalendar() {
        System.out.println("insde the updatecalendar");
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, Constants.currentMonthIndex);
        robotoCalendarView.initializeCalendar(currentCalendar);
        if (Constants.currentMonthIndex == 0) {
            robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
        }
    }

    @Override
    public void onClick(View view) {
        if(view==list){
            Intent i = new  Intent(Schedule_Appointment_Calendar.this , Schedule_Appointment_List.class);
            startActivity(i);
        }
    }

    public class apptlist extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String , String>> result = new ArrayList<HashMap<String , String>>();

        ArrayList localArrayList = new ArrayList();


        protected Void doInBackground(Void... paramVarArgs) {

            //http://phphosting.osvin.net/SalonApp/API/getTimeSlot.php?barber_id=109&date=2015-11-05

            try {
                localArrayList.add(new BasicNameValuePair("barber_id", Constants.STYLIST_USER_ID));
                localArrayList.add(new BasicNameValuePair("date",  Constants.DATE_TO_SEND));

                result = function.apptList(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.size()>0){
                    Constants.apptList.clear();
                    Constants.apptList.addAll(result);
                    mAdapter = new MyAdapter(getApplicationContext(),
                            Constants.apptList);
                    listview.setAdapter(mAdapter);
                } else {
                    try {
                        Constants.apptList.clear();
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    StringUtils.showDialog("No data found.", Schedule_Appointment_Calendar.this);
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, Schedule_Appointment_Calendar.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(Schedule_Appointment_Calendar.this,
                    R.drawable.loader_two);
            db.show();
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
