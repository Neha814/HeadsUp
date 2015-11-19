package fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.headsup.R;

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
public class HomeStylistFragment extends Fragment {

    ListView listview;
    boolean isConnected ;
    TransparentProgressDialog db;
    MyAdapter mAdapter ;
    ImageLoader imageLoader ;
    ArrayList<HashMap<String , String>> myAppointmentList = new ArrayList<HashMap<String , String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_stylist_fragment, container, false);

        isConnected = NetConnection.checkInternetConnectionn(getActivity());
        imageLoader = new ImageLoader(getActivity());

        listview = (ListView) rootView.findViewById(R.id.listview);

        if (isConnected) {
            new myAppointments().execute(new Void[0]);
        } else {
            StringUtils.showDialog(Constants.No_INTERNET, getActivity());
        }

        return rootView;

    }

    public class myAppointments extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String , String>> result = new ArrayList<HashMap<String , String>>();

        ArrayList localArrayList = new ArrayList();


        protected Void doInBackground(Void... paramVarArgs) {

            //http://phphosting.osvin.net/SalonApp/API/viewBookingschedule.php?user_id=109&user_type=stylist

            try {
                localArrayList.add(new BasicNameValuePair("user_id", Constants.USER_ID));
                localArrayList.add(new BasicNameValuePair("user_type",  Constants.USER_TYPE));

                result = function.myApptList(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if(result.size()>0){
                    myAppointmentList.clear();
                    myAppointmentList.addAll(result);
                    mAdapter = new MyAdapter(getActivity(),
                            myAppointmentList);
                    listview.setAdapter(mAdapter);
                } else {

                    StringUtils.showDialog("No data found.",getActivity());
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


        public MyAdapter(Context context,
                         ArrayList<HashMap<String, String>> categoryList) {
            mInflater = LayoutInflater.from(getActivity());


        }


        @Override
        public int getCount() {

            return myAppointmentList.size();
        }

        @Override
        public Object getItem(int position) {
            return myAppointmentList.get(position);
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


                convertView = mInflater.inflate(R.layout.stylist_schedules_listitem, null);

                holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                holder.user_address = (TextView) convertView.findViewById(R.id.user_address);
                holder.appointment_timings = (TextView) convertView.findViewById(R.id.appointment_timings);
                holder.service_name = (TextView) convertView.findViewById(R.id.service_name);
                holder.service_type = (TextView) convertView.findViewById(R.id.service_type);
                holder.service_amount = (TextView) convertView.findViewById(R.id.service_amount);


            convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String dateSlot = myAppointmentList.get(position).get("slot_date");

            String convertedDateSlot = StringUtils.formateDateFromstring("yyyy-MM-dd", "dd MMM, yy",dateSlot);

            holder.user_name.setText(myAppointmentList.get(position).get("display_name"));
            holder.appointment_timings.setText("Your appointment will begin between "+
                    myAppointmentList.get(position).get("from_time")+" to "+ myAppointmentList.get(position).get("to_time")+
            " on "+convertedDateSlot);

            holder.service_name.setText(myAppointmentList.get(position).get("offer_used"));
            holder.service_amount.setText("$ "+myAppointmentList.get(position).get("amount"));

            String profile_url = myAppointmentList.get(position).get("profile_image");

            imageLoader.DisplayImage(profile_url, R.drawable.noimg, holder.user_image);

            return convertView;
        }

        class ViewHolder {
            ImageView user_image;
            TextView user_name, user_address, appointment_timings, service_name, service_type, service_amount;
        }

    }
}
