package Functions;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sandeep on 26/10/15.
 */
public class Functions {

    JSONParser json = new JSONParser();
    public static String url = "http://phphosting.osvin.net/SalonApp/API/";


    /**
     * Registration
     * @param localArrayList
     * @return
     */

    public HashMap signup(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "signup.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("Response");
            if (status.equalsIgnoreCase("true")) {
                localHashMap.put("Response", "true");
                localHashMap.put("Message", localJSONObject.getString("Message"));

                JSONObject result = localJSONObject.getJSONObject("result");
                localHashMap.put("user_id", result.getString("user_id"));
                localHashMap.put("user_name", result.getString("user_name"));
                localHashMap.put("user_email", result.getString("user_email"));
                localHashMap.put("user_type",result.getString("user_type"));
                localHashMap.put("user_promo_code",result.getString("user_promo_code"));

            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("Message", localJSONObject.getString("Message"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }

    /**
     * login
     * @param localArrayList
     * @return
     */

    public HashMap login(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "signin.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("Response");
            if (status.equalsIgnoreCase("true")) {
                localHashMap.put("Response", "true");
                localHashMap.put("Message", localJSONObject.getString("Message"));

                JSONObject result = localJSONObject.getJSONObject("result");
                localHashMap.put("user_id", result.getString("user_id"));
                localHashMap.put("user_name", result.getString("user_name"));
                localHashMap.put("user_email", result.getString("user_email"));
                localHashMap.put("user_type",result.getString("user_type"));
                localHashMap.put("profile_image",result.getString("profile_image"));
                localHashMap.put("user_promo_code",result.getString("user_promo_code"));


            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("Message", localJSONObject.getString("Message"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }

    /**
     * get Stylist List
     * @param localArrayList
     * @return
     */

    public ArrayList<HashMap<String, String>> stylistList(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "barberSearch.php?",
                            "POST", localArrayList)).toString());

            String resopnse = localJSONObject.getString("Response");
            if (resopnse.equalsIgnoreCase("true")) {

                JSONArray Data = localJSONObject.getJSONArray("data");
                for (int i = 0; i < Data.length(); i++) {
                    HashMap<String, String> localhashMap = new HashMap<String, String>();
                    localhashMap.put("id", Data.getJSONObject(i).getString("id"));
                    localhashMap.put("user_id", Data.getJSONObject(i).getString("user_id"));
                    localhashMap.put("signup_type", Data.getJSONObject(i).getString("signup_type"));
                    localhashMap.put("authkey", Data.getJSONObject(i).getString("authkey"));
                    localhashMap.put("user_type", Data.getJSONObject(i).getString("user_type"));
                    localhashMap.put("personal_code", Data.getJSONObject(i).getString("personal_code"));
                    localhashMap.put("refer_id", Data.getJSONObject(i).getString("refer_id"));
                    localhashMap.put("user_bio", Data.getJSONObject(i).getString("user_bio"));
                    localhashMap.put("user_location", Data.getJSONObject(i).getString("user_location"));
                    localhashMap.put("user_address", Data.getJSONObject(i).getString("user_address"));
                    localhashMap.put("user_city", Data.getJSONObject(i).getString("user_city"));
                    localhashMap.put("user_state", Data.getJSONObject(i).getString("user_state"));
                    localhashMap.put("user_country", Data.getJSONObject(i).getString("user_country"));
                    localhashMap.put("user_zip", Data.getJSONObject(i).getString("user_zip"));
                    localhashMap.put("profile_image", Data.getJSONObject(i).getString("profile_image"));
                    localhashMap.put("barber_type", Data.getJSONObject(i).getString("barber_type"));
                    localhashMap.put("display_name", Data.getJSONObject(i).getString("display_name"));

                    localArrayList1.add(localhashMap);

                }

            }
            return localArrayList1;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localArrayList1;

        }

    }

    /**
     *
     * @param localArrayList
     * @return
     */

    public HashMap forgetPassword(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "forgetPassword.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("Response");
            if (status.equalsIgnoreCase("true")) {
                localHashMap.put("Response", "true");
                localHashMap.put("Message", localJSONObject.getString("Message"));




            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("Message", localJSONObject.getString("Message"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }

    public ArrayList<HashMap<String, String>> categoryList(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "getCategories.php?",
                            "POST", localArrayList)).toString());

            String resopnse = localJSONObject.getString("Response");
            if (resopnse.equalsIgnoreCase("true")) {

                JSONArray Data = localJSONObject.getJSONArray("categories");
                for (int i = 0; i < Data.length(); i++) {
                    HashMap<String, String> localhashMap = new HashMap<String, String>();

                    localhashMap.put("term_id", Data.getJSONObject(i).getString("term_id"));
                    localhashMap.put("name", Data.getJSONObject(i).getString("name"));
                    localhashMap.put("slug", Data.getJSONObject(i).getString("slug"));
                    localhashMap.put("term_group", Data.getJSONObject(i).getString("term_group"));
                    localhashMap.put("Price", Data.getJSONObject(i).getString("Price"));
                    localhashMap.put("term_taxonomy_id", Data.getJSONObject(i).getString("term_taxonomy_id"));
                    localhashMap.put("taxonomy", Data.getJSONObject(i).getString("taxonomy"));
                    localhashMap.put("description", Data.getJSONObject(i).getString("description"));
                    localhashMap.put("parent", Data.getJSONObject(i).getString("parent"));
                    localhashMap.put("count", Data.getJSONObject(i).getString("count"));
                    localhashMap.put("cat_ID", Data.getJSONObject(i).getString("cat_ID"));
                    localhashMap.put("category_count", Data.getJSONObject(i).getString("category_count"));
                    localhashMap.put("category_description", Data.getJSONObject(i).getString("category_description"));
                    localhashMap.put("cat_name", Data.getJSONObject(i).getString("cat_name"));
                    localhashMap.put("category_nicename", Data.getJSONObject(i).getString("category_nicename"));
                    localhashMap.put("category_parent", Data.getJSONObject(i).getString("category_parent"));
                    localhashMap.put("Image", Data.getJSONObject(i).getString("MainImage"));
                    localhashMap.put("Icon", Data.getJSONObject(i).getString("Icon"));
                    localhashMap.put("GreyImage", Data.getJSONObject(i).getString("GreyImage"));




                    localArrayList1.add(localhashMap);

                }

            }
            return localArrayList1;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localArrayList1;

        }

    }

    /**
     * to get appt listing
     * @param localArrayList
     * @return
     */

    public ArrayList<HashMap<String, String>> apptList(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "getTimeSlot.php?",
                            "POST", localArrayList)).toString());

            String resopnse = localJSONObject.getString("Response");
            if (resopnse.equalsIgnoreCase("true")) {

                JSONArray Data = localJSONObject.getJSONArray("EmptyTimeSlots");
                for (int i = 0; i < Data.length(); i++) {
                    HashMap<String, String> localhashMap = new HashMap<String, String>();

                    localhashMap.put("keyname", Data.getJSONObject(i).getString("keyname"));

                    localArrayList1.add(localhashMap);

                }

            }
            return localArrayList1;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localArrayList1;

        }

    }

    public HashMap logout(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "logout.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("Response");
            if (status.equalsIgnoreCase("true")) {
                localHashMap.put("Response", "true");
                localHashMap.put("Message", localJSONObject.getString("Message"));




            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("Message", localJSONObject.getString("Message"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }

    /**
     * API for booking schedule
     * @param localArrayList
     * @return
     */

    public HashMap bookSchedule(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "bookingschedule.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("Response");
            if (status.equalsIgnoreCase("true")) {
                localHashMap.put("Response", "true");
                localHashMap.put("Message", localJSONObject.getString("Message"));

            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("Message", localJSONObject.getString("Message"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }

    /**
     * Stylist appointment listing
     * @param localArrayList
     * @return
     */

    public ArrayList<HashMap<String, String>> myApptList(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "viewBookingschedule.php?",
                            "POST", localArrayList)).toString());

            String resopnse = localJSONObject.getString("Response");
            if (resopnse.equalsIgnoreCase("true")) {

                JSONArray Data = localJSONObject.getJSONArray("Result");
                for (int i = 0; i < Data.length(); i++) {
                    HashMap<String, String> localhashMap = new HashMap<String, String>();

                    localhashMap.put("id", Data.getJSONObject(i).getString("id"));
                    localhashMap.put("user_id", Data.getJSONObject(i).getString("user_id"));
                    localhashMap.put("barber_id", Data.getJSONObject(i).getString("barber_id"));
                    localhashMap.put("from_time", Data.getJSONObject(i).getString("from_time"));
                    localhashMap.put("to_time", Data.getJSONObject(i).getString("to_time"));
                    localhashMap.put("slot_date", Data.getJSONObject(i).getString("slot_date"));
                    localhashMap.put("amount", Data.getJSONObject(i).getString("amount"));
                    localhashMap.put("offer_used", Data.getJSONObject(i).getString("offer_used"));
                    localhashMap.put("display_name", Data.getJSONObject(i).getString("display_name"));
                    localhashMap.put("profile_image", Data.getJSONObject(i).getString("profile_image"));


                    localArrayList1.add(localhashMap);

                }

            }
            return localArrayList1;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localArrayList1;

        }

    }

    /**
     * Friend LISt
     * @param localArrayList
     * @return
     */

    public ArrayList<HashMap<String, String>> friendList(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "chatList.php?",
                            "POST", localArrayList)).toString());

            String resopnse = localJSONObject.getString("Response");
            if (resopnse.equalsIgnoreCase("true")) {

                JSONArray Data = localJSONObject.getJSONArray("Result");
                for (int i = 0; i < Data.length(); i++) {
                    HashMap<String, String> localhashMap = new HashMap<String, String>();

                    localhashMap.put("id", Data.getJSONObject(i).getString("id"));
                    localhashMap.put("user_id", Data.getJSONObject(i).getString("user_id"));
                    localhashMap.put("display_name", Data.getJSONObject(i).getString("display_name"));
                    localhashMap.put("profile_image", Data.getJSONObject(i).getString("profile_image"));
                    localhashMap.put("barber_id", Data.getJSONObject(i).getString("barber_id"));


                    localArrayList1.add(localhashMap);

                }

            }
            return localArrayList1;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localArrayList1;

        }

    }

    /**
     * load previous messages
     * @param localArrayList
     * @return
     */


    public ArrayList<HashMap<String, String>> PreviousMessage(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "chat.php?",
                            "POST", localArrayList)).toString());

            String resopnse = localJSONObject.getString("Response");
            if (resopnse.equalsIgnoreCase("true")) {
                    Constants.MESSAGE_RESPONSE = "true";
                JSONArray Data = localJSONObject.getJSONArray("results");
                for (int i = 0; i < Data.length(); i++) {
                    HashMap<String, String> localhashMap = new HashMap<String, String>();

                    localhashMap.put("id", Data.getJSONObject(i).getString("id"));
                    localhashMap.put("from_id", Data.getJSONObject(i).getString("from_id"));
                    localhashMap.put("to_id", Data.getJSONObject(i).getString("to_id"));
                    localhashMap.put("message", Data.getJSONObject(i).getString("message"));
                    localhashMap.put("created", Data.getJSONObject(i).getString("created"));

                    localArrayList1.add(localhashMap);

                }

            } else {
                Constants.MESSAGE_RESPONSE = "false";
            }
            return localArrayList1;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localArrayList1;

        }

    }

    /**
     * Add package
     * @param localArrayList
     * @return
     */

    public HashMap AddPackgae(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "addPackage.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("Response");
            if (status.equalsIgnoreCase("true")) {
                localHashMap.put("Response", "true");
                localHashMap.put("Message", localJSONObject.getString("Message"));


            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("Message", localJSONObject.getString("Message"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }

    /**
     * Gallery Listing
     */

    public ArrayList<HashMap<String, String>> myGallery(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "userImageList.php?",
                            "POST", localArrayList)).toString());

            String resopnse = localJSONObject.getString("Response");
            if (resopnse.equalsIgnoreCase("true")) {

                JSONArray Data = localJSONObject.getJSONArray("Images");
                for (int i = 0; i < Data.length(); i++) {
                    HashMap<String, String> localhashMap = new HashMap<String, String>();

                    localhashMap.put("id", Data.getJSONObject(i).getString("id"));
                    localhashMap.put("user_id", Data.getJSONObject(i).getString("user_id"));
                    localhashMap.put("user_image", Data.getJSONObject(i).getString("user_image"));
                    localhashMap.put("date_created", Data.getJSONObject(i).getString("date_created"));

                    localArrayList1.add(localhashMap);

                }

            }
            return localArrayList1;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localArrayList1;

        }

    }

}
