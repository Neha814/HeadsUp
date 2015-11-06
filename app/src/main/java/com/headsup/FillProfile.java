package com.headsup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import Functions.Constants;
import fragments.Profile1;
import fragments.Profile2;
import utils.HttpClientUpload;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;


/**
 * Created by sandeep on 27/10/15.
 */
public class FillProfile extends FragmentActivity implements View.OnClickListener{

    ViewPager pager;

    private TabsPagerAdapter mAdapter;

    View view1 , view2 ;

    RelativeLayout login_layout;

    Button login;

    updateProfileTask updateProfileObj;

    boolean isConnected;

    TransparentProgressDialog db ;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fill_profile);

        isConnected = NetConnection
                .checkInternetConnectionn(getApplicationContext());

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        inIt();
    }

    private void inIt() {
        pager = (ViewPager) findViewById(R.id.pager);
        view1 = (View) findViewById(R.id.view1);
        view2 = (View) findViewById(R.id.view2);
        login_layout = (RelativeLayout) findViewById(R.id.login_layout);
        login = (Button) findViewById(R.id.login);


        login.setOnClickListener(this);
        login_layout.setOnClickListener(this);

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);

        pager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {

                    view1.setBackgroundResource(R.drawable.grey_circle);
                    view2.setBackgroundResource(R.drawable.white_circle);

                } else if (position == 1) {
                    view1.setBackgroundResource(R.drawable.white_circle);
                    view2.setBackgroundResource(R.drawable.grey_circle);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==login || view==login_layout){
            int count = getItem(+1);

            Boolean isTrueORFalse;

            if(count==1) {
                isTrueORFalse = Profile1.checkDetailIsFilledOrNot();
                if(isTrueORFalse){
                    pager.setCurrentItem(getItem(+1), true);
                }
            } else if(count==2){
                isTrueORFalse = Profile2.checkDetailIsFilledOrNot();
                if(isTrueORFalse){
                    CallProfileUpdateAPI();
                }
            }
        }
    }

    private void CallProfileUpdateAPI() {

        if (isConnected) {
            updateProfileObj = new updateProfileTask();
            updateProfileObj.execute();

        } else {
            StringUtils.showDialog(Constants.No_INTERNET, FillProfile.this);
        }
    }

    protected int getItem(int i) {
        return pager.getCurrentItem() + i;
    }

    //************************** update Profile ******************************************//


    public class updateProfileTask extends AsyncTask<String, Void, String> {
        ByteArrayOutputStream baos;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(FillProfile.this,
                    R.drawable.loader_two);
            db.show();

        }

        @Override
        protected String doInBackground(String... Params) {

            try {
                baos = new ByteArrayOutputStream();
                Constants.BITMAP_TO_SEND.compress(Bitmap.CompressFormat.PNG, 100, baos);


            } catch (Exception e) {
                Log.e("excptn==", "" + e);
            }

			/*
			 * http://phphosting.osvin.net/divineDistrict/api/userOptions.php?
			 * user_id
			 * =31&authKey=divineDistrict@31&radius=30&organisations=7,8&image=
			 */

            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpClientUpload client = new HttpClientUpload(
                        "http://phphosting.osvin.net/SalonApp/API/personalDetails.php?");
                client.connectForMultipart();

                /*http://phphosting.osvin.net/SalonApp/API/personalDetails.php
                userId,user_bio,profile_image,address,city,location,state,country,zip,barber_type*/

                client.addFormPart("userId", Constants.USER_ID);
                client.addFormPart("user_bio",Constants.BIO);
                client.addFormPart("address",Constants.ADDRESS);
                client.addFormPart("city", Constants.CITY);
                client.addFormPart("location", Constants.LOCATION);
                client.addFormPart("state", Constants.STATE);
                client.addFormPart("country",Constants.COUNTRY);

                client.addFormPart("zip", Constants.ZIPCODE);
                client.addFormPart("barber_type", "");

                if (!(Constants.FILE_TO_SEND.getName().equals("") || Constants.FILE_TO_SEND
                        .getName() == null)) {

                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;

                    int maxBufferSize = 5 * 1024 * 1024;

                    FileInputStream fileInputStream = new FileInputStream(Constants.FILE_TO_SEND);
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    client.addFilePart("profile_image", Constants.FILE_TO_SEND.getName(),
                            buffer);
                }

                client.finishMultipart();

                String data = client.getResponse();

                Log.e("data==", "" + data);

                return data;
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            db.dismiss();

         //   Sorry, this file type is not permitted for security reasons.{"Response":true,"Message":"Profile update Successfully"}

            try {

                JSONObject localJSONObject = new JSONObject(result);
                String status = localJSONObject.getString("Response");
                if (status.equalsIgnoreCase("true")) {
                    Toast.makeText(getApplicationContext(),"Profile update Successfully",Toast.LENGTH_SHORT).show();
                   String userType = sp.getString("user_type","");
                    if(userType.equals("user")) {
                        Intent i = new Intent(FillProfile.this, HomeUser.class);
                        startActivity(i);
                        finish();
                    } else if(userType.equals("stylist")){
                        Intent i = new Intent(FillProfile.this, HomeStylist.class);
                        startActivity(i);
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Error occurred. Please try after some time.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(FillProfile.this , Login.class);
                    startActivity(i);
                    finish();
                }


            } catch (Exception e) {
                Log.e("Exception===", "" + e);
                StringUtils.showDialog(Constants.ERROR_MSG,FillProfile.this);
            }
        }

    }
}
