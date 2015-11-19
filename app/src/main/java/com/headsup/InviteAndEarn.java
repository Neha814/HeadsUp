package com.headsup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Functions.Constants;

/**
 * Created by sandeep on 29/10/15.
 */
public class InviteAndEarn extends Activity implements View.OnClickListener {

    TextView static_text;
    TextView promo_code;
    ImageView message_icon, gmail_icon,facebook_icon,whatsapp_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.invite_earn);

        inIt();
    }

    private void inIt() {
        static_text = (TextView) findViewById(R.id.static_text);
        promo_code = (TextView) findViewById(R.id.promo_code);
        message_icon = (ImageView) findViewById(R.id.message_icon);
        gmail_icon = (ImageView) findViewById(R.id.gmail_icon);
        facebook_icon = (ImageView) findViewById(R.id.facebook_icon);
        whatsapp_icon = (ImageView) findViewById(R.id.whatsapp_icon);

        message_icon.setOnClickListener(this);
        gmail_icon.setOnClickListener(this);
        facebook_icon.setOnClickListener(this);
        whatsapp_icon.setOnClickListener(this);


        promo_code.setText(Constants.USER_PROMO_CODE);

        SpannableString content1 = new SpannableString("Don't worry, we never share your personal information.");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        static_text.setText(content1);

    }

    @Override
    public void onClick(View view) {

        if(view==message_icon){
            MessageSharing();

        } else if(view==gmail_icon){
            googleSharing();

        } else if(view == facebook_icon){
            fbSharing();

        } else if(view == whatsapp_icon){
            twitterSharing();
        }
    }

    // Message sharing

    protected void MessageSharing() {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", "#HeadsUp - Check out this amazing app!!!");
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);

        } catch (Exception ex) {
            Toast.makeText(InviteAndEarn.this,
                    ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    // google sharing

    protected void googleSharing() {
        try {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent share = new Intent(
                    android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            List<ResolveInfo> resInfo = this
                    .getPackageManager()
                    .queryIntentActivities(share, 0);
            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    Intent targetedShare = new Intent(
                            android.content.Intent.ACTION_SEND);
                    targetedShare.setType("text/plain"); // put here
                    // your mime
                    // type
                    if (info.activityInfo.packageName.toLowerCase()
                            .contains("plus")
                            || info.activityInfo.name.toLowerCase()
                            .contains("plus")) {
                        System.out
                                .println("inside the if conditionsssss");
                        targetedShare.putExtra(Intent.EXTRA_SUBJECT,
                                "#HeadsUp");
                        targetedShare.putExtra(Intent.EXTRA_TEXT,
                                "Check Out this amazing app...");
                        targetedShare
                                .setPackage(info.activityInfo.packageName);
                        targetedShareIntents.add(targetedShare);
                    }
                }
                Intent chooserIntent = Intent.createChooser(
                        targetedShareIntents.remove(0),
                        "Select app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        targetedShareIntents
                                .toArray(new Parcelable[] {}));
                startActivity(chooserIntent);
            }
        } catch (Exception e) {
            Log.v("VM", "Exception while sending image on" + "plus"
                    + " " + e.getMessage());
        }
    }

    // twitter sharing

    protected void twitterSharing() {
        try {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "It's a Tweet!"
                    + "#HeadsUp");
            intent.setType("text/plain");
            final PackageManager pm = this.getPackageManager();
            final List<?> activityList = pm.queryIntentActivities(
                    intent, 0);
            int len = activityList.size();
            for (int i = 0; i < len; i++) {

                final ResolveInfo app = (ResolveInfo) activityList
                        .get(i);

                if ((app.activityInfo.name.contains("twitter"))) {
                    Log.i("twitter==<>", "" + app.activityInfo.name);

                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName x = new ComponentName(
                            activity.applicationInfo.packageName,
                            activity.name);

                    intent = new Intent(Intent.ACTION_SEND);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    intent.setComponent(x);

                    intent.putExtra(Intent.EXTRA_TEXT,
                            "It's a tweet #HeadsUp");
                    intent.setType("application/twitter");
                    startActivity(intent);

                    break;

                } else {
                    if (i + 1 == len) {
                        String link = "https://play.google.com/store/apps/details?id=com.twitter.android&hl=en";
                        Log.e("else else", "else else");
                        showDailog(link);
                        break;
                    }

                }
            }
        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

    // facebook sharing

    protected void fbSharing() {
        try {

            String urlToShare = "http://www.google.com";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
            final PackageManager pm = this.getPackageManager();
            final List<?> activityList = pm.queryIntentActivities(
                    intent, 0);
            int len = activityList.size();
            for (int i = 0; i < len; i++) {

                final ResolveInfo app = (ResolveInfo) activityList
                        .get(i);
                Log.i("<>==<>", "" + app.activityInfo.packageName);
                if (app.activityInfo.packageName.toLowerCase()
                        .startsWith("com.facebook.katana")) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u="
                            + urlToShare;
                    intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(sharerUrl));
                    startActivity(intent);

                    break;

                } else {
                    if (i + 1 == len) {
                        String link = "https://play.google.com/store/apps/details?id=com.twitter.android&hl=en";
                        Log.e("else else", "else else");
                        showDailog(link);
                        break;
                    }

                }
            }
        } catch (Exception ae) {
            ae.printStackTrace();
        }

    }


    private void showDailog(final String link) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InviteAndEarn.this);
        alertDialog.setTitle("Logout...");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
