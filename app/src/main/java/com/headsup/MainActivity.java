package com.headsup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import Functions.Constants;

public class MainActivity extends AppCompatActivity {

    String PROJECT_NUMBER = "307455181893";
    GoogleCloudMessaging gcm;
    String regid;
    SharedPreferences sp;
    boolean inHome = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        inHome = sp.getBoolean("inHome",false);

        Thread t = new Thread() {
            public void run() {
                try {
                    getRegID();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private String getRegID() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = regid;

                    Log.e("GCM", msg);

                }
                catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();


                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Constants.REGISTRATION_ID = msg;

                Log.e("user_type==>",""+sp.getString("user_type",""));

                if(inHome) {
                    Constants.USER_TYPE = sp.getString("user_type","");
                    if(Constants.USER_TYPE.equals("user")){
                        Intent i = new Intent(MainActivity.this, HomeUser.class);
                        startActivity(i);
                        finish();
                    } else if(Constants.USER_TYPE.equals("stylist")){
                        Intent i = new Intent(MainActivity.this, HomeStylist.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                    finish();
                }

            }
        }.execute(null, null, null);
        return regid;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeUser/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
