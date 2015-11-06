package com.headsup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import Functions.Constants;
import Functions.Functions;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 3/11/15.
 */
public class User_Details extends Activity implements View.OnClickListener{

    TextView our_services, type, price, date, our_offers;
    ListView listview;
    boolean isConnected ;
    TransparentProgressDialog db;
    MyAdapter mAdapter ;
    EditText search_edittext;
    ArrayList<HashMap<String , String>> categoryList = new ArrayList<HashMap<String , String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_details);

        inIt();
    }

    private void inIt() {
        our_services = (TextView) findViewById(R.id.our_services);
        type = (TextView) findViewById(R.id.type);
        price = (TextView) findViewById(R.id.price);
        date = (TextView) findViewById(R.id.date);
        our_offers = (TextView) findViewById(R.id.our_offers);
        listview = (ListView) findViewById(R.id.listview);
        search_edittext = (EditText) findViewById(R.id.search_edittext);

        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());

        our_services.setOnClickListener(this);
        type.setOnClickListener(this);
        price.setOnClickListener(this);
        date.setOnClickListener(this);
        our_offers.setOnClickListener(this);


        if (isConnected) {
            new categorylist().execute(new Void[0]);
        } else {
            StringUtils.showDialog(Constants.No_INTERNET, getApplicationContext());
        }

        search_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String text = search_edittext.getText().toString()
                            .toLowerCase(Locale.getDefault());
                    mAdapter.filter(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String mainBgUrl = categoryList.get(i).get("Image");
                String iconUrl = categoryList.get(i).get("Icon");
                String iconGreyUrl = categoryList.get(i).get("GreyImage");

                String mainBGArray[] = mainBgUrl.split("@/");
                String iconArray[] = iconUrl.split("@/");
                String iconGreyArray[] = iconGreyUrl.split("@/");

                Constants.SELECTED_CAT_SLUG = categoryList.get(i).get("slug");
                Constants.SELECTED_CAT_NAME = categoryList.get(i).get("name");
                Constants.SELECTED_CAT_DESCRIPTION = categoryList.get(i).get("description");
                Constants.SELECTED_CAT_IMAGE_URL = mainBGArray[0];
                Constants.SELECTED_CAT_ICON_URL = iconArray[0];
                Constants.SELECTED_CAT_GREYICON_URL = iconGreyArray[0];

                Intent intent = new Intent(User_Details.this, ServiceType.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==our_services){
            Intent i = new Intent(User_Details.this , OurServices.class);
            startActivity(i);
        }

    }

    public class categorylist extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String , String>> result = new ArrayList<HashMap<String , String>>();

        ArrayList localArrayList = new ArrayList();


        protected Void doInBackground(Void... paramVarArgs) {

            //http://phphosting.osvin.net/SalonApp/API/getCategories.php?category=main

            try {
                localArrayList.add(new BasicNameValuePair("category", "main"));

                result = function.categoryList(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.size()>0){
                    categoryList.addAll(result);
                    mAdapter = new MyAdapter(getApplicationContext(),
                            categoryList);
                    listview.setAdapter(mAdapter);
                } else {
                    StringUtils.showDialog("No data found.", User_Details.this);
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, User_Details.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(User_Details.this,
                    R.drawable.loader_two);
            db.show();
        }

    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater mInflater = null;

        private ArrayList<HashMap<String, String>> mDisplayedValues;

        public MyAdapter(Context context,
                         ArrayList<HashMap<String, String>> categoryList) {
            mInflater = LayoutInflater.from(getApplicationContext());

            mDisplayedValues = new ArrayList<HashMap<String, String>>();
            mDisplayedValues.addAll(categoryList);
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());

            categoryList.clear();
            if (charText.length() == 0) {

                categoryList.addAll(mDisplayedValues);
            } else {

                for (int i = 0 ;i<mDisplayedValues.size();i++) {

                    if (mDisplayedValues.get(i).get("name").toLowerCase(Locale.getDefault())
                            .startsWith(charText)) {


                        categoryList.add(mDisplayedValues.get(i));

                    }
                }
            }
            notifyDataSetChanged();

        }

        @Override
        public int getCount() {

            return categoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return categoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

             if (convertView == null) {
            holder = new ViewHolder();



                convertView = mInflater.inflate(R.layout.category_listitem, null);

            holder.cat_name = (TextView) convertView.findViewById(R.id.cat_name);
                 if(position %2==0){
                     holder.cat_name.setBackgroundColor(Color.parseColor("#dcdcdc"));
                 } else {
                     holder.cat_name.setBackgroundColor(Color.parseColor("#ffffff"));
                 }


            convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.cat_name.setText(categoryList.get(position).get("name"));


            return convertView;
        }

        class ViewHolder {
            TextView cat_name ;

        }

    }
}
