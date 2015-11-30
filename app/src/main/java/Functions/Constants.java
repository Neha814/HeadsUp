package Functions;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sandeep on 21/10/15.
 */
public class Constants {

    public static String REGISTRATION_ID ;
    public static String No_INTERNET  = "No internet connection." ;
    public static String ERROR_MSG = "Error occurred. Please try again.";


    //************ Register Screen ******************//

    public static String SIGNUP_TYPE ;
    public static String USER_TYPE ;
    public static String USER_ID ;
    public static String USER_NAME ;
    public static String USER_EMAIL ;
    public static boolean IS_COMING_FROM_FB_GOOGLE;
    public static String FB_GOOGLE_PRO_PIC_URL ;
    public static String USERNAME_REGISTER="" ;
    public static String EMAIL;
    public static String PROFILE_PIC;
    public static String USER_PROMO_CODE;


    public static String PERSON_NAME_FB_GOOGLE;
    public static String PERSON_EMAIL_FB_GOOGLE;

    //********* update profile *********************//

    public static File FILE_TO_SEND = new File("");
    public static Bitmap BITMAP_TO_SEND;
    public static String LOCATION;
    public static String ADDRESS;
    public static String CITY;
    public static String STATE;
    public static String COUNTRY;
    public static String ZIPCODE;
    public static String  BIO;

    //********* HomeUserFragment *******************//

    public static String STYLIST_USER_ID ;
    public static String STYLIST_NAME ;
    public static String SALOON_ADDRESS ;
    public static String STYLIST_PIC_URL;

    //******** Our Services **********************//

    public static String SELECTED_CAT_NAME ;
    public static String SELECTED_CAT_SLUG;
    public static String SELECTED_CAT_DESCRIPTION;
    public static String SELECTED_CAT_IMAGE_URL;
    public static String SELECTED_CAT_ICON_URL;
    public static String SELECTED_CAT_GREYICON_URL;

    // stylist
    public static String SELECTED_CAT_ID;

    //******** Service Type ********************//

    public static String SERVICE_TYPE_NAME;
    public static String SERVICE_TYPE_PRICE;

    // *********** calendar *******************//

    public static int currentMonth = 1;

    public static int currentMonthIndex = 0;

    // *********** scehdule_appt / Listing ***************//

    public static ArrayList<HashMap<String , String>> apptList = new ArrayList<HashMap<String , String>>();

    public static String DATE_TO_SEND;
    public static String DATE_TO_SHOW;
    public static String BOOKED_TIME_SLOT;

    // ************* Chat screen **********************//

    public static String SENDER_NAME ;
    public static String SENDER_ID ;
    public static String SENDER_PIC ;
    public static String RECEIVER_NAME ;
    public static String RECEIVER_ID ;
    public static String RECEIVER_PIC ;
    public static String MESSAGE ;

    public static String MESSAGE_RESPONSE ;

    // ************ gallery screen **********************//

    public static String IMAGE_URL_TO_SHARE ;



}
