package com.headsup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import Functions.Constants;

/**
 * Created by sandeep on 28/10/15.
 */
public class RegisterFbGoogle extends Activity implements View.OnClickListener {

    ImageButton user,stylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        inIt();
    }

    private void inIt() {

        user = (ImageButton) findViewById(R.id.user);
        stylist = (ImageButton) findViewById(R.id.stylist);

        Constants.USER_TYPE = "";

        user.setOnClickListener(this);
        stylist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==user){
            Constants.USER_TYPE = "user";
            finish();
        } else if(view==stylist){
            Constants.USER_TYPE = "stylist";
            finish();
        }
    }
}
