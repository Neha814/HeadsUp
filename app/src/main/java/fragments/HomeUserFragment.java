package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.headsup.R;
import com.headsup.User_Details;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Functions.Constants;
import Functions.Functions;
import imageLoader.ImageLoader;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 29/10/15.
 */
public class HomeUserFragment extends Fragment {

    Button filter_button;
    ImageView filter_img;
    ListView listview;
    boolean isConnected ;
    TransparentProgressDialog db;
    MyAdapter mAdapter ;
    ArrayList<HashMap<String , String>> stylistList = new ArrayList<HashMap<String , String>>();
    ImageLoader imageLoader ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_stylist_fragment, container, false);

        filter_img = (ImageView) rootView.findViewById(R.id.filter_img);
        filter_button = (Button) rootView.findViewById(R.id.filter_button);
        listview = (ListView) rootView.findViewById(R.id.listview);

        isConnected = NetConnection.checkInternetConnectionn(getActivity());
        imageLoader = new ImageLoader(getActivity());

        if (isConnected) {
            new stylistlist().execute(new Void[0]);
        } else {
            StringUtils.showDialog(Constants.No_INTERNET, getActivity());
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String  user_location = stylistList.get(i).get("user_location");
                String  user_address = stylistList.get(i).get("user_address");
                String  user_city = stylistList.get(i).get("user_city");
                String  user_state = stylistList.get(i).get("user_state");
                String  user_country = stylistList.get(i).get("user_country");
                String  user_zip = stylistList.get(i).get("user_zip");

                List<String> addList = new ArrayList<String>();
                addList.add(user_location);
                addList.add(user_address);
                addList.add(user_city);
                addList.add(user_state);
                addList.add(user_country);
                addList.add(user_zip);


                addList.removeAll(Arrays.asList("", null));

                String ADDRESS_TEXT = addList.toString().replace("[", "")
                        .replace("]", "").replace(", ", ", ");

                Constants.STYLIST_USER_ID = stylistList.get(i).get("user_id");
                Constants.STYLIST_NAME = stylistList.get(i).get("display_name");
                Constants.SALOON_ADDRESS = ADDRESS_TEXT;
                Constants.STYLIST_PIC_URL=  stylistList.get(i).get("profile_image");

                Intent intent = new Intent(getActivity() , User_Details.class);
                startActivity(intent );
            }
        });

        return  rootView;

    }

    public class stylistlist extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String , String>> result = new ArrayList<HashMap<String , String>>();

        ArrayList localArrayList = new ArrayList();


        protected Void doInBackground(Void... paramVarArgs) {

            // http://phphosting.osvin.net/SalonApp/API/barberSearch.php?stylist=

            try {
                localArrayList.add(new BasicNameValuePair("stylist", ""));

                result = function.stylistList(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.size()>0){
                    stylistList.addAll(result);
                    mAdapter = new MyAdapter(getActivity(),
                            stylistList);
                    listview.setAdapter(mAdapter);
                } else {
                    StringUtils.showDialog("No data found.", getActivity());
                }

            }

            catch (Exception ae) {
                ae.printStackTrace();
                StringUtils.showDialog(Constants.ERROR_MSG, getActivity());
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(getActivity(),
                    R.drawable.loader_two);
            db.show();
        }

    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater mInflater = null;

        private ArrayList<HashMap<String, String>> mDisplayedValues;

        public MyAdapter(Context context,
                         ArrayList<HashMap<String, String>> categoryList) {
            mInflater = LayoutInflater.from(getActivity());

            mDisplayedValues = new ArrayList<HashMap<String, String>>();
            mDisplayedValues.addAll(stylistList);
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());

            stylistList.clear();
            if (charText.length() == 0) {

                stylistList.addAll(mDisplayedValues);
            } else {

                for (int i = 0 ;i<mDisplayedValues.size();i++) {

                    if (mDisplayedValues.get(i).get("fname").toLowerCase(Locale.getDefault())
                            .startsWith(charText)) {


                        stylistList.add(mDisplayedValues.get(i));

                    }
                }
            }
            notifyDataSetChanged();

        }

        @Override
        public int getCount() {

            return stylistList.size();
        }

        @Override
        public Object getItem(int position) {
            return stylistList.get(position);
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

               if( position % 2 == 0) {

                   convertView = mInflater.inflate(R.layout.select_stylist_listitem_left, null);
               } else {
                   convertView = mInflater.inflate(R.layout.select_stylist_listitem_right, null);
               }

                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.stylist_type = (TextView) convertView.findViewById(R.id.stylist_type);
                holder.work_exp = (TextView) convertView.findViewById(R.id.work_exp);
                holder.profile_pic = (ImageView) convertView.findViewById(R.id.profile_pic);


                convertView.setTag(holder);
            /*} else {
                holder = (ViewHolder) convertView.getTag();
            }*/

            String  user_location = stylistList.get(position).get("user_location");
            String  user_address = stylistList.get(position).get("user_address");
            String  user_city = stylistList.get(position).get("user_city");
            String  user_state = stylistList.get(position).get("user_state");
            String  user_country = stylistList.get(position).get("user_country");
            String  user_zip = stylistList.get(position).get("user_zip");
            String profile_url = stylistList.get(position).get("profile_image");

            List<String> addList = new ArrayList<String>();
            addList.add(user_location);
            addList.add(user_address);
            addList.add(user_city);
            addList.add(user_state);
            addList.add(user_country);
            addList.add(user_zip);


            addList.removeAll(Arrays.asList("", null));

            String ADDRESS_TEXT = addList.toString().replace("[", "")
                    .replace("]", "").replace(", ", ", ");

            if(ADDRESS_TEXT.length()>1){
                holder.address.setText(ADDRESS_TEXT);
            } else {
                holder.address.setText("Address not mentioned.");
            }



            holder.user_name.setText(stylistList.get(position).get("display_name"));

            holder.stylist_type.setText(stylistList.get(position).get("user_bio"));

            imageLoader.DisplayImage(profile_url, R.drawable.noimg, holder.profile_pic);

            return convertView;
        }

        class ViewHolder {
            TextView user_name ,address,stylist_type,work_exp ;
            ImageView profile_pic;
        }

    }
}
