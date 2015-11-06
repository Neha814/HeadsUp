package com.headsup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import Functions.Constants;
import Functions.Functions;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 21/10/15.
 */
public class RegisterForm extends Activity implements View.OnClickListener {

    TextView terms_conditions;

    TextView privacy;

    TextView login_here;

    CheckBox agree_checkbox;

    EditText username;

    EditText email;

    EditText password;

    RelativeLayout register_layout;

    Button register;

    boolean isCheckboxChecked = false ;

    boolean isConnected;

    TransparentProgressDialog db;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.register_form);
        
        inIt();
    }

    private void inIt() {

        terms_conditions = (TextView) findViewById(R.id.terms_conditions);
        privacy = (TextView) findViewById(R.id.privacy);
        login_here = (TextView) findViewById(R.id.login_here);
        agree_checkbox = (CheckBox) findViewById(R.id.agree_checkbox);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register_layout = (RelativeLayout) findViewById(R.id.register_layout);
        register = (Button) findViewById(R.id.register);


        login_here.setOnClickListener(this);
        register.setOnClickListener(this);
        register_layout.setOnClickListener(this);

        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        SpannableString content1 = new SpannableString("Terms & Conditions");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        terms_conditions.setText(content1);

        SpannableString content2 = new SpannableString("Privacy policy");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        privacy.setText(content2);

        SpannableString content3 = new SpannableString("Login Here");
        content3.setSpan(new UnderlineSpan(), 0, content3.length(), 0);
        login_here.setText(content3);

        agree_checkbox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            isCheckboxChecked = true;
                            agree_checkbox
                                    .setButtonDrawable(R.drawable.box_checked);
                        } else {
                            isCheckboxChecked = false;
                            agree_checkbox
                                    .setButtonDrawable(R.drawable.box_uncheck);
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view==login_here){
            Intent i = new Intent(RegisterForm.this , Login.class);
            startActivity(i);
            finish();
        }
        else if(view==register || view==register_layout){
            ValidateAndRegister();
        }
    }

    /**
     * Validate all the fields before calling registeration API
     */

    private void ValidateAndRegister() {
        String username_text = username.getText().toString();
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();

        if(username_text.trim().length()<1){
            username.setError("Please enter username");
        } else if(email_text.trim().length()<1){
            email.setError("Please enter email address");
        } else if(!(StringUtils.verify(email_text))){
            email.setError("Please enter valid email address.");
        }else if(password_text.trim().length()<6){
            password.setError("Please enter password having minimum length of 6.");
        }
        /*else if(!StringUtils.isAlphaNumeric(password_text)){
            password.setError("Password must contain atleast one special character and number.");
        }*/
        else {
            if(isCheckboxChecked) {
                if (isConnected) {
                    Constants.SIGNUP_TYPE = "";

                    new RegisterTask(username_text, email_text, password_text).execute(new Void[0]);
                } else {
                    StringUtils.showDialog(Constants.No_INTERNET, RegisterForm.this);
                }
            } else {
                Toast.makeText(getApplicationContext(),"Please accept terms & conditions",Toast.LENGTH_SHORT).show();
            }

        }
    }

    // ************************************ Register Task *****************************************//

    public class RegisterTask extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();

        String uSERNAME, pASSWORD, eMAIL;

        public RegisterTask(String username , String email , String password) {

            this.uSERNAME = username;
            this.eMAIL = email;
            this.pASSWORD = password;
        }

        protected Void doInBackground(Void... paramVarArgs) {

            /*http://phphosting.osvin.net/SalonApp/API/signup.php?full_name=adam singh
             &user_email=abc@gmail.com&password=adam@123&signup_type=facebook&user_type=barber&profile_image=*/

            try {
                localArrayList.add(new BasicNameValuePair("full_name",
                        this.uSERNAME));
                localArrayList.add(new BasicNameValuePair("user_email",
                        this.eMAIL));
                localArrayList
                        .add(new BasicNameValuePair("password", this.pASSWORD));
                localArrayList.add(new BasicNameValuePair("signup_type",
                        Constants.SIGNUP_TYPE));
                localArrayList.add(new BasicNameValuePair("user_type",
                      Constants.USER_TYPE));
                localArrayList.add(new BasicNameValuePair("profile_image",
                        ""));



                result = function.signup(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.get("Response").equals("true")){
                    Editor e = sp.edit();
                    e.putString("user_id", (String) result.get("user_id"));
                    e.putString("user_email", (String) result.get("user_email"));
                    e.putString("user_name", (String) result.get("user_name"));
                    e.putString("user_type",(String) result.get("user_type"));

                    e.commit();
                    Constants.USER_ID = sp.getString("user_id","");
                    Constants.USERNAME_REGISTER = this.uSERNAME;
                    Constants.EMAIL = sp.getString("user_email","");
                    Constants.USER_TYPE = sp.getString("user_type","");

                    Log.e("user_type(regster)", "" + Constants.USER_TYPE);

                    Toast.makeText(getApplicationContext(),"Register successfully.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterForm.this , FillProfile.class);
                    startActivity(i);


                }
                else {
                    String msg = (String)result.get("Message");
                    StringUtils.showDialog(msg, RegisterForm.this);
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, RegisterForm.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(RegisterForm.this,
                    R.drawable.loader_two);
            db.show();
        }

    }
}
