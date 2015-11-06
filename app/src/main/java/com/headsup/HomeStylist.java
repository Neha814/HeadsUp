package com.headsup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import Functions.Constants;

/**
 * Created by sandeep on 28/10/15.
 */
public class HomeStylist extends Activity {

    SharedPreferences sp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_stylist);

        inIt();


    }

    private void inIt() {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Constants.USER_ID = sp.getString("user_id", "");
        Constants.USERNAME_REGISTER = sp.getString("user_name","");
        Constants.EMAIL = sp.getString("user_email","");
        Constants.USER_TYPE = sp.getString("user_type","");
        Constants.PROFILE_PIC = sp.getString("profile_image","");

        Log.e("user_type==>", "" + Constants.USER_TYPE);

        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("inHome",true);
        e.commit();
    }
}
