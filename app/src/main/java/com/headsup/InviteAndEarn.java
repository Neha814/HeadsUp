package com.headsup;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * Created by sandeep on 29/10/15.
 */
public class InviteAndEarn extends Activity {

    TextView static_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.invite_earn);

        inIt();
    }

    private void inIt() {
        static_text = (TextView) findViewById(R.id.static_text);

        SpannableString content1 = new SpannableString("Don't worry, we never share your personal information.");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        static_text.setText(content1);

    }
}
