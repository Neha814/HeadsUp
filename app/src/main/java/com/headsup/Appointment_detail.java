package com.headsup;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import Functions.Constants;
import Functions.Functions;
import imageLoader.ImageLoader;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 5/11/15.
 */
public class Appointment_detail  extends Activity implements View.OnClickListener{

    ImageView stylist_image, grey_icon;
    TextView stylist_name, saloon_address, appointment_timings, cat_name, service_type, price, remove, total_amount, pay_now;
    LinearLayout pay_now_layout;
    ImageLoader imageLoader ;
    boolean isConnected;

    TransparentProgressDialog db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appt_detail);

        inIt();
    }

    private void inIt() {

        imageLoader = new ImageLoader(getApplicationContext());

        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());

        stylist_image = (ImageView) findViewById(R.id.stylist_image);
        grey_icon = (ImageView) findViewById(R.id.grey_icon);
        stylist_name = (TextView) findViewById(R.id.stylist_name);
        saloon_address = (TextView) findViewById(R.id.saloon_address);
        appointment_timings = (TextView) findViewById(R.id.appointment_timings);
        cat_name = (TextView) findViewById(R.id.cat_name);
        service_type = (TextView) findViewById(R.id.service_type);
        price = (TextView) findViewById(R.id.price);
        remove = (TextView) findViewById(R.id.remove);
        total_amount = (TextView) findViewById(R.id.total_amount);
        pay_now = (TextView) findViewById(R.id.pay_now);
        pay_now_layout =(LinearLayout) findViewById(R.id.pay_now_layout);

        pay_now.setOnClickListener(this);
        pay_now_layout.setOnClickListener(this);

        imageLoader.DisplayImage(Constants.STYLIST_PIC_URL, R.drawable.noimg, stylist_image);
        imageLoader.DisplayImage(Constants.SELECTED_CAT_GREYICON_URL, R.drawable.noimg, grey_icon);

        stylist_name.setText(Constants.STYLIST_NAME);
        saloon_address.setText(Constants.SALOON_ADDRESS);
        cat_name.setText(Constants.SELECTED_CAT_NAME);
        service_type.setText(Constants.SERVICE_TYPE_NAME);
        stylist_name.setText(Constants.STYLIST_NAME);
        price.setText("$ "+Constants.SERVICE_TYPE_PRICE);
        total_amount.setText("$ "+Constants.SERVICE_TYPE_PRICE);
        appointment_timings.setText("Your appointment will begin between "+Constants.BOOKED_TIME_SLOT+" on "+Constants.DATE_TO_SHOW);


    }

    @Override
    public void onClick(View view) {
        if(view==pay_now || view ==pay_now_layout){

            if (isConnected) {

                new BookSchedule().execute(new Void[0]);

            } else {

                StringUtils.showDialog(Constants.No_INTERNET, Appointment_detail.this);
            }

        }
    }

    // API for booking schedule



    public class BookSchedule extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();


        String timeArray[] = Constants.BOOKED_TIME_SLOT.split("-");

        String fromTime = timeArray[0];
        String toTime = timeArray[1];




        protected Void doInBackground(Void... paramVarArgs) {

    /*http://phphosting.osvin.net/SalonApp/API/bookingschedule.php?user_id=117&barber_id=118
     &from_time=11:00&to_time=12:00&slot_date=2016-07-15&transaction_id=asdasdasd&amount=2512&offer_used=bvnb*/

            /*if(fromTime.contains("AM")){
                fromTime =  fromTime.replace("AM","");
            } else {
                fromTime = fromTime.replace("PM","");
            }

            if(toTime.contains("A.M")){
                toTime = toTime.replace("AM","");
            } else {
                toTime = toTime.replace("PM","");
            }*/


            try {
                localArrayList.add(new BasicNameValuePair("user_id",Constants.USER_ID ));
                localArrayList.add(new BasicNameValuePair("barber_id",Constants.STYLIST_USER_ID ));
                localArrayList.add(new BasicNameValuePair("from_time",this.fromTime ));
                localArrayList.add(new BasicNameValuePair("to_time",this.toTime));
                localArrayList.add(new BasicNameValuePair("slot_date",Constants.DATE_TO_SEND));
                localArrayList.add(new BasicNameValuePair("transaction_id","" ));
                localArrayList.add(new BasicNameValuePair("amount", Constants.SERVICE_TYPE_PRICE));
                localArrayList.add(new BasicNameValuePair("offer_used", Constants.SELECTED_CAT_NAME+" "+Constants.SERVICE_TYPE_NAME));

                result = function.bookSchedule(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.get("Response").equals("true")){
                     Intent intent = new Intent(Appointment_detail.this , Appointment_confirmation.class);
            startActivity(intent);
                }
                else {
                    String msg = (String)result.get("Message");
                    StringUtils.showDialog(msg, Appointment_detail.this);
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, Appointment_detail.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(Appointment_detail.this,
                    R.drawable.loader_two);
            db.show();
        }

    }
}
