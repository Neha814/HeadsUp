package com.headsup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import Functions.Constants;

/**
 * Created by sandeep on 5/11/15.
 */
public class Appointment_confirmation extends Activity {

    TextView saloon_address ,appointment_timings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appt_confirmation);

        inIt();
    }

    private void inIt() {

        appointment_timings = (TextView) findViewById(R.id.appointment_timings);
        saloon_address = (TextView) findViewById(R.id.saloon_address);

        saloon_address.setText(Constants.SALOON_ADDRESS);
        appointment_timings.setText("See you on "+Constants.DATE_TO_SHOW+" at "+Constants.BOOKED_TIME_SLOT);
    }
}
