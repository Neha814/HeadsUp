package com.headsup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import Functions.Constants;
import Functions.Functions;
import fragments.StylistServicesFragments;
import imageLoader.ImageLoader;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 28/10/15.
 */
public class HomeStylist extends FragmentActivity implements View.OnClickListener {

    SharedPreferences sp ;
    LinearLayout menu_layout;
    ImageView menu_img;
    ImageLoader imageLoader ;
    boolean isClicked = false ;
    TransparentProgressDialog db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_stylist);

        inIt();


    }

    private void inIt() {

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        imageLoader = new ImageLoader(getApplicationContext());

        menu_img = (ImageView) findViewById(R.id.menu_img);
        menu_layout = (LinearLayout) findViewById(R.id.menu_layout);

        menu_img.setOnClickListener(this);
        menu_layout.setOnClickListener(this);

        Constants.USER_ID = sp.getString("user_id", "");
        Constants.USERNAME_REGISTER = sp.getString("user_name","");
        Constants.EMAIL = sp.getString("user_email","");
        Constants.USER_TYPE = sp.getString("user_type","");
        Constants.PROFILE_PIC = sp.getString("profile_image","");
        Constants.USER_PROMO_CODE = sp.getString("user_promo_code","");

        Log.e("user_type==>", "" + Constants.USER_TYPE);

        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("inHome",true);
        e.commit();

        FragmentManager fm  = HomeStylist.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;
      //  fragment = new HomeStylistFragment();
        fragment = new StylistServicesFragments();
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();



    }


    @Override
    public void onClick(View view) {
        if(view == menu_img || view == menu_layout){
            ShowMenuDialog();
        }
    }

    private void ShowMenuDialog() {
        final Dialog dialog;
        LinearLayout cancel_layout , home_layout;
        ImageView cancel_img ,home_img;
        TextView user_name;
        RelativeLayout setting_layout ,logout_layout,invite_layout,about_laoyut,rating_layout,chat_layout,
                my_appt_layout;
        ImageView setting_img , logout_img,invite_img,about_img,rating_img,profile_pic,chat_img,my_appt_img;

        dialog = new Dialog(HomeStylist.this, R.style.full_screen_dialog);
        dialog.setCancelable(true);
        if (isClicked == false) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        isClicked = true;

        dialog.setContentView(R.layout.stylist_menu);

        cancel_layout = (LinearLayout) dialog.findViewById(R.id.cancel_layout);
        home_layout = (LinearLayout) dialog.findViewById(R.id.home_layout);
        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        home_img = (ImageView) dialog.findViewById(R.id.home_img);
        setting_layout = (RelativeLayout) dialog.findViewById(R.id.setting_layout);
        logout_layout = (RelativeLayout) dialog.findViewById(R.id.logout_layout);
        setting_img = (ImageView) dialog.findViewById(R.id.setting_img);
        logout_img = (ImageView) dialog.findViewById(R.id.logout_img);
        invite_img = (ImageView) dialog.findViewById(R.id.invite_img);
        invite_layout = (RelativeLayout) dialog.findViewById(R.id.invite_layout);
        about_img = (ImageView) dialog.findViewById(R.id.about_img);
        about_laoyut = (RelativeLayout) dialog.findViewById(R.id.about_laoyut);
        rating_layout = (RelativeLayout) dialog.findViewById(R.id.rating_layout);
        rating_img = (ImageView) dialog.findViewById(R.id.rating_img);
        profile_pic = (ImageView) dialog.findViewById(R.id.profile_pic);
        user_name = (TextView) dialog.findViewById(R.id.user_name);
        chat_img = (ImageView) dialog.findViewById(R.id.chat_img);
        chat_layout = (RelativeLayout) dialog.findViewById(R.id.chat_layout);
        my_appt_img = (ImageView) dialog.findViewById(R.id.my_appt_img);
        my_appt_layout  = (RelativeLayout) dialog.findViewById(R.id.my_aapt_layout);


        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(145);
        dialog.getWindow().setBackgroundDrawable(d);

        dialog.getWindow().getAttributes().windowAnimations =

                R.style.dialog_animation_top;
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

        imageLoader.DisplayImage(Constants.PROFILE_PIC, R.drawable.noimg, profile_pic);
        user_name.setText(Constants.USERNAME_REGISTER);

        //new LoadProfileImage(profile_pic).execute(Constants.PROFILE_PIC);

        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        home_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        setting_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , Settings.class);
                startActivity(i);

            }
        });
        setting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , Settings.class);
                startActivity(i);
            }
        });

        invite_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , InviteAndEarn.class);
                startActivity(i);
            }
        });

        invite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , InviteAndEarn.class);
                startActivity(i);
            }
        });

        about_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , AboutUs.class);
                startActivity(i);

            }
        });
        about_laoyut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , AboutUs.class);
                startActivity(i);

            }
        });

        logout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showLogoutLayout();
            }
        });

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showLogoutLayout();
            }
        });

        rating_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , RatingAndReviews.class);
                startActivity(i);
            }
        });

        rating_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , RatingAndReviews.class);
                startActivity(i);
            }
        });

        chat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , ChatList.class);
                startActivity(i);
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , ChatList.class);
                startActivity(i);
            }
        });
        my_appt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , StylistMyAppointment.class);
                startActivity(i);
            }
        });

        my_appt_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(HomeStylist.this , StylistMyAppointment.class);
                startActivity(i);
            }
        });

    }

    private void showLogoutLayout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeStylist.this);
        alertDialog.setTitle("Logout...");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

                // call logout API
                new Logout().execute(new Void[0]);


            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public class Logout extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();

        String eMAIL;

        protected Void doInBackground(Void... paramVarArgs) {

            //http://phphosting.osvin.net/SalonApp/API/logout.php?user_id=adam&token=asdasdas

            try {
                localArrayList.add(new BasicNameValuePair("user_id",
                        Constants.USER_ID));
                localArrayList.add(new BasicNameValuePair("token",
                        Constants.REGISTRATION_ID));

                result = function.logout(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.get("Response").equals("true")){
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean("inHome", false);
                    e.commit();
                    Intent i = new Intent(HomeStylist.this, Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, HomeStylist.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(HomeStylist.this,
                    R.drawable.loader_two);
            db.show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(HomeStylist.this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
