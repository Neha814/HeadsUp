package com.headsup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import Functions.Constants;
import Functions.Functions;
import imageLoader.ImageLoader;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 4/11/15.
 */
public class ServiceType extends Activity implements View.OnClickListener{

    TextView cat_name, service_title,service_description, next;
    ImageView main_image, service_img;
    ListView listview;
    RelativeLayout next_layout;
    ImageLoader imageLoader ;

    boolean isConnected ;
    TransparentProgressDialog db;
    MyAdapter mAdapter ;
    ArrayList<HashMap<String , String>> categoryList = new ArrayList<HashMap<String , String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_type);

        inIt();
    }

    private void inIt() {

        imageLoader = new ImageLoader(getApplicationContext());
        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());

        cat_name = (TextView) findViewById(R.id.cat_name);
        service_title = (TextView) findViewById(R.id.service_title);
        service_description = (TextView) findViewById(R.id.service_description);
        next = (TextView) findViewById(R.id.next);
        main_image = (ImageView) findViewById(R.id.main_image);
        service_img = (ImageView) findViewById(R.id.service_img);
        listview = (ListView) findViewById(R.id.listview);
        next_layout = (RelativeLayout) findViewById(R.id.next_layout);

        next.setOnClickListener(this);
        next_layout.setOnClickListener(this);

        cat_name.setText(Constants.SELECTED_CAT_NAME);
        service_title.setText(Constants.SELECTED_CAT_NAME);
        service_description.setText(Constants.SELECTED_CAT_DESCRIPTION);

        imageLoader.DisplayImage(Constants.SELECTED_CAT_IMAGE_URL, R.drawable.noimg, main_image);
        imageLoader.DisplayImage(Constants.SELECTED_CAT_ICON_URL, R.drawable.noimg, service_img);

        if (isConnected) {
            new categorylist().execute(new Void[0]);
        } else {
            StringUtils.showDialog(Constants.No_INTERNET, getApplicationContext());
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Constants.SERVICE_TYPE_NAME = categoryList.get(i).get("name");
                Constants.SERVICE_TYPE_PRICE = categoryList.get(i).get("Price");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view== next || view==next_layout){
            Intent i = new Intent(ServiceType.this , Schedule_Appointment_Calendar.class);
            startActivity(i);

        }

    }

    public class categorylist extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String , String>> result = new ArrayList<HashMap<String , String>>();

        ArrayList localArrayList = new ArrayList();


        protected Void doInBackground(Void... paramVarArgs) {

            //http://phphosting.osvin.net/SalonApp/API/getCategories.php?category=facial

            try {
                localArrayList.add(new BasicNameValuePair("category", Constants.SELECTED_CAT_SLUG));

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
                    StringUtils.showDialog("No data found.", ServiceType.this);
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, ServiceType.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(ServiceType.this,
                    R.drawable.loader_two);
            db.show();
        }

    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater mInflater = null;

        public MyAdapter(Context context,
                         ArrayList<HashMap<String, String>> categoryList) {
            mInflater = LayoutInflater.from(getApplicationContext());

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

                 convertView = mInflater.inflate(R.layout.service_type_listitem, null);

                holder.type = (TextView) convertView.findViewById(R.id.type);
                 holder.price = (TextView) convertView.findViewById(R.id.price);

            convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.type.setText(categoryList.get(position).get("name"));
            holder.price.setText(categoryList.get(position).get("Price")+" $");

            return convertView;
        }

        class ViewHolder {
            TextView type, price;

        }

    }
}
