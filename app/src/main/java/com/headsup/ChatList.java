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
 * Created by sandeep on 16/11/15.
 */
public class ChatList extends Activity {

    ListView listview;
    boolean isConnected ;
    TransparentProgressDialog db;
    MyAdapter mAdapter ;
    ImageLoader imageLoader ;
    ArrayList<HashMap<String , String>> FriendList = new ArrayList<HashMap<String , String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_list);

        inIt();
    }

    private void inIt() {

        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
        imageLoader = new ImageLoader(getApplicationContext());

        listview = (ListView) findViewById(R.id.listview);

        if (isConnected) {
            new FriendList().execute(new Void[0]);
        } else {
            StringUtils.showDialog(Constants.No_INTERNET, getApplicationContext());
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constants.SENDER_ID = Constants.USER_ID;
                Constants.SENDER_NAME = Constants.USER_NAME;
                Constants.SENDER_PIC = Constants.PROFILE_PIC;

                if(Constants.USER_TYPE.equalsIgnoreCase("user")){
                    Constants.RECEIVER_ID = FriendList.get(i).get("barber_id");
                } else {
                    Constants.RECEIVER_ID = FriendList.get(i).get("user_id");
                }


                Constants.RECEIVER_NAME = FriendList.get(i).get("display_name");
                Constants.RECEIVER_PIC = FriendList.get(i).get("profile_image");

                Intent intent = new Intent(ChatList.this , ChatScreen.class);
                startActivity(intent);
            }
        });
    }

    public class FriendList extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String , String>> result = new ArrayList<HashMap<String , String>>();

        ArrayList localArrayList = new ArrayList();


        protected Void doInBackground(Void... paramVarArgs) {

            //http://phphosting.osvin.net/SalonApp/API/chatList.php?user_id=117&user_type=user

            try {
                localArrayList.add(new BasicNameValuePair("user_id", Constants.USER_ID));
                localArrayList.add(new BasicNameValuePair("user_type",  Constants.USER_TYPE));

                result = function.friendList(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.size()>0){
                    FriendList.clear();
                    FriendList.addAll(result);
                    mAdapter = new MyAdapter(getApplicationContext(),
                            FriendList);
                    listview.setAdapter(mAdapter);
                } else {

                    StringUtils.showDialog("No data found.",getApplicationContext());
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, getApplicationContext());
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(getApplicationContext(),
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

            return FriendList.size();
        }

        @Override
        public Object getItem(int position) {
            return FriendList.get(position);
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


                convertView = mInflater.inflate(R.layout.chat_listitem, null);

                holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                holder.address = (TextView) convertView.findViewById(R.id.address);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.user_name.setText(FriendList.get(position).get("display_name"));
            String profile_url = FriendList.get(position).get("profile_image");
            imageLoader.DisplayImage(profile_url, R.drawable.noimg, holder.user_image);



            return convertView;
        }

        class ViewHolder {
            ImageView user_image;
            TextView user_name;
            TextView address;
        }

    }
}
