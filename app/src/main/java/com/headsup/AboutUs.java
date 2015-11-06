package com.headsup;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * Created by sandeep on 29/10/15.
 */
public class AboutUs extends Activity {

    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_us);

        inIt();
    }

    private void inIt() {

        email = (TextView) findViewById(R.id.email);

        SpannableString content1 = new SpannableString("contact@headsup.com");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        email.setText(content1);
    }
}
