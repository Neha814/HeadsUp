package com.headsup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
 * Created by sandeep on 3/11/15.
 */
public class OurServices extends Activity {
    ListView listview;
    boolean isConnected ;
    TransparentProgressDialog db;
    MyAdapter mAdapter ;
    ArrayList<HashMap<String , String>> categoryList = new ArrayList<HashMap<String , String>>();
    ImageLoader imageLoader ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.our_services);
        inIt();

    }

    private void inIt() {

        listview = (ListView) findViewById(R.id.listview);
        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
        imageLoader = new ImageLoader(getApplicationContext());

        if (isConnected) {
            new categorylist().execute(new Void[0]);
        } else {
            StringUtils.showDialog(Constants.No_INTERNET, getApplicationContext());
        }

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
                Constants.SELECTED_CAT_NAME= categoryList.get(i).get("name");
                Constants.SELECTED_CAT_DESCRIPTION =categoryList.get(i).get("description");
                Constants.SELECTED_CAT_IMAGE_URL = mainBGArray[0];
                Constants.SELECTED_CAT_ICON_URL =iconArray[0];
                Constants.SELECTED_CAT_GREYICON_URL = iconGreyArray[0];

                Intent intent = new Intent(OurServices.this , ServiceType.class);
                startActivity(intent);

            }
        });
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
                    StringUtils.showDialog("No data found.", OurServices.this);
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, OurServices.this);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(OurServices.this,
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

           // if (convertView == null) {
                holder = new ViewHolder();

                if(position %2==0){
                    convertView = mInflater.inflate(R.layout.our_service_listitem_left, null);
                } else {
                    convertView = mInflater.inflate(R.layout.our_service_listitem_right, null);
               }



            holder.service_title = (TextView) convertView.findViewById(R.id.service_title);
            holder.service_description = (TextView) convertView.findViewById(R.id.service_description);
            holder.main_image = (ImageView) convertView.findViewById(R.id.main_image);
            holder.service_img = (ImageView) convertView.findViewById(R.id.service_img);



                convertView.setTag(holder);
           /* } else {
                holder = (ViewHolder) convertView.getTag();
            }*/

            String mainBgUrl = categoryList.get(position).get("Image");

            String iconUrl = categoryList.get(position).get("Icon");

            String mainBGArray[] = mainBgUrl.split("@/");
            String iconArray[] = iconUrl.split("@/");

            Log.e("1", "" + mainBGArray[0]);
            Log.e("2", "" + iconArray[0]);

            holder.service_title.setText(categoryList.get(position).get("name"));
            holder.service_description.setText(categoryList.get(position).get("description"));
            imageLoader.DisplayImage(mainBGArray[0], R.drawable.noimg, holder.main_image);
            imageLoader.DisplayImage(iconArray[0], R.drawable.noimg, holder.service_img);


            return convertView;
        }

        class ViewHolder {
            ImageView main_image, service_img;
            TextView service_title, service_description;

        }

    }
}
