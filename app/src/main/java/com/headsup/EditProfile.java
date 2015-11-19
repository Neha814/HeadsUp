package com.headsup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Functions.Constants;
import imageLoader.ImageLoader;
import utils.NetConnection;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 16/11/15.
 */
public class EditProfile extends Activity {

    ImageView profile_pic, edit;
    TextView user_name;
    Button update;
    RelativeLayout update_layout;
    ImageLoader imageLoader ;
    boolean isConnected ;
    TransparentProgressDialog db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_profile);

        inIt();
    }

    private void inIt() {

        imageLoader = new ImageLoader(getApplicationContext());
        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());

        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        edit = (ImageView) findViewById(R.id.edit);
        user_name = (TextView) findViewById(R.id.user_name);
        update = (Button) findViewById(R.id.update);
        update_layout = (RelativeLayout) findViewById(R.id.update_layout);

        imageLoader.DisplayImage(Constants.PROFILE_PIC, R.drawable.noimg, profile_pic);
        user_name.setText(Constants.USERNAME_REGISTER);
    }
}
