package com.headsup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sandeep on 29/10/15.
 */
public class Settings extends Activity implements View.OnClickListener {

    RelativeLayout change_password_layout;
    TextView change_password;
    ImageView password_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        inIt();
    }

    private void inIt() {
        change_password_layout = (RelativeLayout) findViewById(R.id.change_password_layout);
        change_password = (TextView) findViewById(R.id.change_password);
        password_arrow = (ImageView) findViewById(R.id.password_arrow);

        change_password_layout.setOnClickListener(this);
        change_password.setOnClickListener(this);
        password_arrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==change_password || view==change_password_layout || view==password_arrow){
            Intent i = new Intent(Settings.this , ChnagePassword.class);
            startActivity(i);
        }
    }
}
