package com.headsup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import utils.NetConnection;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 29/10/15.
 */
public class ChnagePassword extends Activity implements View.OnClickListener {

    EditText old_password , new_password,confrim_new_password;
    ImageView save_img;
    TextView save_text;
    boolean isConnected ;
    TransparentProgressDialog db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        inIt();
    }

    private void inIt() {

        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        confrim_new_password = (EditText) findViewById(R.id.confrim_new_password);
        save_text = (TextView) findViewById(R.id.save_text);
        save_img = (ImageView) findViewById(R.id.save_img);

        save_img.setOnClickListener(this);
        save_text.setOnClickListener(this);

        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        if(view==save_img || view==save_text){
            String old_password_text = old_password.getText().toString();
            String new_password_text = new_password.getText().toString();
            String confirm_password_text = confrim_new_password.getText().toString();

            if(old_password_text.trim().length()<1){
                old_password.setError("Please enter current password");
            }
            else if(new_password_text.trim().length()<1){
                new_password.setError("Please enter new password");
            }
            else if(confirm_password_text.trim().length()<1){
                confrim_new_password.setError("Please enter confirm password");
            } else if(new_password_text.equals(confirm_password_text)){
                // call change password API
                /*if (isConnected) {
                    new ForgetPasswordAPI(email_text).execute(new Void[0]);
                } else {
                    StringUtils.showDialog(Constants.No_INTERNET, ForgetPassword.this);
                }*/
            }
        }
    }
}
