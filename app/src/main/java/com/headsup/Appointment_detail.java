package com.headsup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Functions.Constants;
import imageLoader.ImageLoader;

/**
 * Created by sandeep on 5/11/15.
 */
public class Appointment_detail  extends Activity implements View.OnClickListener{

    ImageView stylist_image, grey_icon;
    TextView stylist_name, saloon_address, appointment_timings, cat_name, service_type, price, remove, total_amount, pay_now;
    LinearLayout pay_now_layout;
    ImageLoader imageLoader ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appt_detail);

        inIt();
    }

    private void inIt() {

        imageLoader = new ImageLoader(getApplicationContext());

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
            Intent intent = new Intent(Appointment_detail.this , Appointment_confirmation.class);
            startActivity(intent);
        }
    }
}
