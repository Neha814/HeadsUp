package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.headsup.R;

import java.util.List;
import java.util.Locale;

import Functions.Constants;
import utils.GPSTracker;

/**
 * Created by sandeep on 27/10/15.
 */
public class Profile2 extends Fragment implements View.OnClickListener {

    GPSTracker gps;

    double lat, lng;

    Geocoder geocoder;

    List<Address> addresses;

    ImageView location_img;

    EditText location_et;

    static EditText address;

    static EditText city;

    static EditText state ;

    static EditText country ;

    static EditText zipcode ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_2, container, false);

        gps = new GPSTracker(getActivity());
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        location_img = (ImageView) rootView.findViewById(R.id.location_img);
        location_et = (EditText) rootView.findViewById(R.id.location_et);
        address = (EditText) rootView.findViewById(R.id.address);
        city = (EditText) rootView.findViewById(R.id.city);
        state = (EditText) rootView.findViewById(R.id.state);
        country = (EditText) rootView.findViewById(R.id.country);
        zipcode = (EditText) rootView.findViewById(R.id.zipcode);

        location_img.setOnClickListener(this);

        location_et.setEnabled(false);



        return rootView;
    }

    private void getAddressFromLatLng() {
        try {

           if (gps.canGetLocation()) {
                lat = gps.getLatitude();
                lng = gps.getLongitude();

            }

            addresses = geocoder.getFromLocation(lat,
                    lng, 1);
           String countryCode = ((Address) addresses.get(0))
                    .getCountryCode();
            String locality = ((Address) addresses.get(0))
                    .getLocality();
            String addressString = ((Address) addresses.get(0))
                    .getAddressLine(0);
            String cityString = ((Address) addresses.get(0))
                    .getAddressLine(1);
            String countryString = ((Address) addresses.get(0))
                    .getAddressLine(2);

            String address_to_set = (addressString + " " + cityString
                    + " " + countryString).replace("null", "");

            Log.e("address_to_set", ""+address_to_set);

            location_et.setText(address_to_set);

            Constants.LOCATION = address_to_set ;


        } catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if(view == location_img) {
            if (!((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled("gps")) {
                showGPSDisabledAlertToUser();

            } else {
                    getAddressFromLatLng();

            }
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
        localBuilder
                .setMessage(
                        "GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface paramAnonymousDialogInterface,
                                    int paramAnonymousInt) {

                                Intent localIntent2 = new Intent(
                                        "android.settings.LOCATION_SOURCE_SETTINGS");
                                startActivityForResult(localIntent2, 1);

                            }
                        });
        localBuilder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            Log.e("req code==","1");
            gps = new GPSTracker(getActivity());
            if (gps.canGetLocation()) {
                Log.e("getAddress", "getAddress");
                lat = gps.getLatitude();
                lng = gps.getLongitude();

                Log.e("lat", "=="+lat);
                Log.e("lng", "=="+lng);

            }

            getAddressFromLatLng();

        }
    }

    public static Boolean checkDetailIsFilledOrNot() {
        String address_text = address.getText().toString();
        String city_text = city.getText().toString();
        String state_text = state.getText().toString();
        String country_text = country.getText().toString();
        String zipcode_text = zipcode.getText().toString();

        if(address_text.trim().length()<1){
            address.setError("Please enter address.");
            return false ;
        }
        else if(city_text.trim().length()<1){
            city.setError("Please enter city.");
            return false ;
        }
        else if(state_text.trim().length()<1){
            state.setError("Please enter state.");
            return false ;
        }
        else if(country_text.trim().length()<1){
            country.setError("Please enter country.");
            return false ;
        }
        else if(zipcode_text.trim().length()<1){
            zipcode.setError("Please enter zipcode.");
            return false ;
        }
        else {
            Constants.ADDRESS = address_text;
            Constants.CITY = city_text;
            Constants.STATE = state_text;
            Constants.COUNTRY = country_text;
            Constants.ZIPCODE = zipcode_text;

            Log.e(" Constants.ADDRESS",""+ Constants.ADDRESS);
            Log.e(" Constants.CITY",""+ Constants.CITY);
            Log.e(" Constants.STATE",""+ Constants.STATE);
            Log.e(" Constants.COUNTRY",""+ Constants.COUNTRY);
            Log.e(" Constants.ZIPCODE",""+ Constants.ZIPCODE);

            return true;
        }
    }
}
