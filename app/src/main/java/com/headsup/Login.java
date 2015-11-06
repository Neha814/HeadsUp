package com.headsup;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Functions.Constants;
import Functions.Functions;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 21/10/15.
 */
public class Login extends Activity implements View.OnClickListener , ConnectionCallbacks, OnConnectionFailedListener {

    TextView register_here ;

    TextView forgot_password;

    RelativeLayout login_layout;

    Button login;

    EditText email;

    EditText password;

    ImageButton google_plus;

    ImageButton fb ;

    boolean isConnected;

    TransparentProgressDialog db;

    SharedPreferences sp;

    String FB_APP_ID = "1513712528949226";

    CallbackManager callbackManager;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private static final int RC_SIGN_IN = 0;

    private static final int PROFILE_PIC_SIZE = 400;

    private ConnectionResult mConnectionResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);



        inIT();


    }
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void inIT() {

        callbackManager = CallbackManager.Factory.create();



        forgot_password = (TextView) findViewById(R.id.forgot_password);
        register_here = (TextView) findViewById(R.id.register_here);
        login_layout = (RelativeLayout) findViewById(R.id.login_layout);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        google_plus = (ImageButton) findViewById(R.id.google_plus);
        login = (Button) findViewById(R.id.login);
        fb = (ImageButton) findViewById(R.id.fb);


        register_here.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        google_plus.setOnClickListener(this);
        login.setOnClickListener(this);
        login_layout.setOnClickListener(this);
        fb.setOnClickListener(this);

        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


        SpannableString content1 = new SpannableString("Register Here");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        register_here.setText(content1);

        SpannableString content2 = new SpannableString("Forgot Password ?");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        forgot_password.setText(content2);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        if (AccessToken.getCurrentAccessToken() != null) {
                            RequestData();

                        }

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Login.this, "Login Cancel", Toast.LENGTH_LONG).show();
                        Log.d("Login Cancel", "Login Cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("FacebookException", ""+exception.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view==register_here){
            Constants.IS_COMING_FROM_FB_GOOGLE = false;
           Intent i = new Intent(Login.this , Register.class);
            startActivity(i);
        }
        else if(view ==login || view == login_layout ){
            Constants.SIGNUP_TYPE = "";
            ValidateAndLogin();
        } else if(view==fb){
            Constants.SIGNUP_TYPE = "facebook";
            LoginWithFacebook();

          /*  Intent i = new Intent(Login.this , RegisterFbGoogle.class);
            startActivityForResult(i, 1);*/

        } else if(view==google_plus){
           Constants.SIGNUP_TYPE = "google plus";
            signInWithGplus();
            /*Intent i = new Intent(Login.this , RegisterFbGoogle.class);
            startActivityForResult(i, 2);*/

        } else if(view ==forgot_password){
            Intent i = new Intent(Login.this , ForgetPassword.class);
            startActivity(i);
        }
    }

    private void signInWithGplus() {
        Log.e("g+ clicked","g+ clicked");
        if (!mGoogleApiClient.isConnecting()) {
            Log.e("g+ isConnecting","g+ isConnecting");
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void LoginWithFacebook() {
        Log.e("fb clicked","fb clicked");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends","public_profile email"));
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


    private void ValidateAndLogin() {

        String email_text = email.getText().toString();
        String password_text = password.getText().toString();

        if(email_text.trim().length()<1){
            email.setError("Please enter email address");
        }else if(!(StringUtils.verify(email_text))){
            email.setError("Please enter valid email address.");
        }
        else if(password_text.trim().length()<1){
            password.setError("Please enter password");
        }else {
            if (isConnected) {

                new LoginTask("",email_text, password_text).execute(new Void[0]);
            } else {
                StringUtils.showDialog(Constants.No_INTERNET, Login.this);
            }
        }
    }


    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        /*details_txt.setText(Html.fromHtml(text));
                        profile.setProfileId(json.getString("id"));*/

                        String name = json.getString("name");
                        String email = json.getString("email");
                        String profile = json.getString("link");
                        String id = json.getString("id");

                        String FB_PIC_URL =  "https://graph.facebook.com/" + id + "/picture?type=normal";

                        Constants.FB_GOOGLE_PRO_PIC_URL = FB_PIC_URL;

                        Constants.IS_COMING_FROM_FB_GOOGLE = true;

                    //    new RegisterTask(name, email, "" ).execute(new Void[0]);

                        LoginManager.getInstance().logOut();

                        new LoginTask(name,email, "").execute(new Void[0]);

                        Log.e("profile==>>",""+profile);
                        Log.e("name==>>",""+name);
                        Log.e("email==>>",""+email);
                        Log.e("FB_PIC_URL==>>",""+FB_PIC_URL);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }


    //***************************** Mannual Login API ******************************************//

    public class LoginTask extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();

        String  pASSWORD, eMAIL , dEVICEID ,personNAME;

        public LoginTask(String personName ,String email , String password) {

            this.eMAIL = email;
            this.pASSWORD = password;
            this.personNAME = personName;

            dEVICEID = Settings.Secure.getString(Login.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        protected Void doInBackground(Void... paramVarArgs) {

            /*http://phphosting.osvin.net/SalonApp/API/signin.php?
             token=asdasd&device_id=sdfsdff34&user_email=gjh@jhg.com&password=123123&signup_type=facebook*/
            try {

                localArrayList.add(new BasicNameValuePair("user_email",
                        this.eMAIL));
                localArrayList
                        .add(new BasicNameValuePair("password", this.pASSWORD));
                localArrayList.add(new BasicNameValuePair("signup_type", Constants.SIGNUP_TYPE));
                localArrayList.add(new BasicNameValuePair("token", Constants.REGISTRATION_ID));
                localArrayList.add(new BasicNameValuePair("device_id", this.dEVICEID));


                result = function.login(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.get("Response").equals("true")){
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("user_id", (String) result.get("user_id"));
                    e.putString("user_email", (String) result.get("user_email"));
                    e.putString("user_name", (String) result.get("user_name"));
                    e.putString("user_type",(String) result.get("user_type"));
                    e.putString("profile_image", (String) result.get("profile_image"));

                    e.commit();

                    Constants.USER_ID = sp.getString("user_id","");
                    Constants.USERNAME_REGISTER = sp.getString("user_name","");
                    Constants.EMAIL = sp.getString("user_email","");
                    Constants.USER_TYPE = sp.getString("user_type","");
                    Constants.PROFILE_PIC =  sp.getString("profile_image","");

                    Toast.makeText(getApplicationContext(), "login successfully.", Toast.LENGTH_SHORT).show();
                    if( Constants.USER_TYPE.equalsIgnoreCase("user")) {
                        Intent i = new Intent(Login.this, HomeUser.class);
                        startActivity(i);
                        finish();
                    }else if(Constants.USER_TYPE.equalsIgnoreCase("stylist")){
                        Intent i = new Intent(Login.this, HomeStylist.class);
                        startActivity(i);
                        finish();
                    }

                }
                else {
                    if(Constants.SIGNUP_TYPE.equals("facebook")){
                        Constants.PERSON_NAME_FB_GOOGLE = personNAME;
                        Constants.PERSON_EMAIL_FB_GOOGLE = eMAIL;
                        Intent i = new Intent(Login.this , RegisterFbGoogle.class);
                        startActivityForResult(i, 1);
                      //  new RegisterTask(Constants.PERSON_NAME_FB_GOOGLE, Constants.PERSON_EMAIL_FB_GOOGLE, "" ).execute(new Void[0]);

                    } else if(Constants.SIGNUP_TYPE.equals("google plus")){
                        Constants.PERSON_NAME_FB_GOOGLE = personNAME;
                        Constants.PERSON_EMAIL_FB_GOOGLE = eMAIL;
                        Intent i = new Intent(Login.this , RegisterFbGoogle.class);
                        startActivityForResult(i, 2);
                       // new RegisterTask(personNAME, eMAIL, "" ).execute(new Void[0]);
                    } else {
                        String msg = (String) result.get("Message");
                        StringUtils.showDialog(msg, Login.this);
                    }
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, Login.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(Login.this,
                    R.drawable.loader_two);
            db.show();
        }

    }


    //***************************** Register (FB / google) *********************************************//


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
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("user_id", (String) result.get("user_id"));
                    e.putString("user_email", (String) result.get("user_email"));
                    e.putString("user_name", (String) result.get("user_name"));
                    e.putString("user_type",(String) result.get("user_type"));

                    e.commit();

                    Constants.USER_ID = sp.getString("user_id","");
                    Constants.USERNAME_REGISTER = this.uSERNAME;
                    Constants.EMAIL = sp.getString("user_email","");
                    Constants.USER_TYPE = sp.getString("user_type","");

                    Toast.makeText(getApplicationContext(),"Register successfully.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this , FillProfile.class);
                    startActivity(i);


                }
                else {
                   // new LoginTask(this.eMAIL, "").execute(new Void[0]);
                    String msg = (String)result.get("Message");
                    StringUtils.showDialog(msg , Login.this);

                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, Login.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(Login.this,
                    R.drawable.loader_two);
            db.show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult","**onActivityResult**");

        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else if(requestCode==1 || requestCode==2){
           // LoginWithFacebook();
            if(Constants.USER_TYPE.length()>1) {
                new RegisterTask(Constants.PERSON_NAME_FB_GOOGLE, Constants.PERSON_EMAIL_FB_GOOGLE, "").execute(new Void[0]);
            } else {
                Toast.makeText(getApplicationContext(),"Please select user type",Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Log.e("else","**else**");
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }


    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();



    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e("info==>>", "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);


                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                //****** signOutFromGPlus ******** //

                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();

                    Constants.FB_GOOGLE_PRO_PIC_URL = personPhotoUrl;

                    Constants.IS_COMING_FROM_FB_GOOGLE = true;

                    new LoginTask(personName,email, "").execute(new Void[0]);

                  //  new RegisterTask(personName, email, "" ).execute(new Void[0]);

                }



            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();

    }
}
