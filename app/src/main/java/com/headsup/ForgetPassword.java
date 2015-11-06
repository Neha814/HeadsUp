package com.headsup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import Functions.Constants;
import Functions.Functions;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 30/10/15.
 */
public class ForgetPassword extends Activity implements View.OnClickListener {

    EditText email;
    TextView done_text;
    ImageView done_img;
    boolean isConnected ;
    TransparentProgressDialog db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forget_password);

        inIt();

    }

    private void inIt() {

        email = (EditText) findViewById(R.id.email);
        done_img = (ImageView) findViewById(R.id.done_img);
        done_text = (TextView) findViewById(R.id.done_text);

        done_text.setOnClickListener(this);
        done_img.setOnClickListener(this);

        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        if(view==done_img || view==done_text){

            String email_text = email.getText().toString();

            if(email_text.trim().length()<1){
                email.setError("Please enter email address");
            } else if(!(StringUtils.verify(email_text))){
                email.setError("Please enter valid email address.");
            }

            else {
                if (isConnected) {
                    new ForgetPasswordAPI(email_text).execute(new Void[0]);
                } else {
                    StringUtils.showDialog(Constants.No_INTERNET, ForgetPassword.this);
                }
            }
        }
    }

    public class ForgetPasswordAPI extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();

        String eMAIL;

        public ForgetPasswordAPI(String email ) {
            this.eMAIL = email;
        }

        protected Void doInBackground(Void... paramVarArgs) {

         //   http://phphosting.osvin.net/SalonApp/API/forget-password.php?email=osvinandroid@gmail.com&username=push

            try {
                localArrayList.add(new BasicNameValuePair("email",
                        this.eMAIL));

                result = function.forgetPassword(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
           db.dismiss();

            try {
                if(result.get("Response").equals("true")){
                    String msg = (String)result.get("Message");
                    showMessageDialog(msg);
                }
                else {

                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, ForgetPassword.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(ForgetPassword.this,
                    R.drawable.loader_two);
            db.show();
        }

    }

    public  void showMessageDialog(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    ForgetPassword.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Alert !");

            // Setting Dialog Message
            alertDialog.setMessage(msg);

            // Setting Icon to Dialog
            //	alertDialog.setIcon(R.drawable.browse);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    dialog.cancel();
                    finish();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
