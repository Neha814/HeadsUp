package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

	public static String replaceWords(String phoneNumber) {

		String added_phoneNo = phoneNumber.replace(" ", "").replace("+", "")
				.replace("-", "").replace("(", "").replace(")", "");
		// if(added_phoneNo.length() > 10) {
		// added_phoneNo = added_phoneNo.substring(added_phoneNo.length() - 10);
		//
		// }
		return added_phoneNo;

	}
	
	/**
	 * Email verification
	 * @param paramString
	 * @return
	 */

	public static boolean verify(String paramString) {
		return paramString
				.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
	}
	
	/**
	 * Date Format conversion
	 * @param dateToConvert
	 * @return
	 */

	public static String DateConverter(String dateToConvert) {

		String dateConvert = dateToConvert;
		String DateConverted = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date myDate = new Date();
		try {
			myDate = dateFormat.parse(dateConvert);
			Log.e("***** myDate *****", "" + myDate);
			SimpleDateFormat formatter = new SimpleDateFormat("MM / dd / yyyy");
			 DateConverted = formatter.format(myDate);
			Log.e("*conveted date *", "" + DateConverted);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return DateConverted;

	}

	public static boolean isAlphaNumeric(String s) {
		String pattern= "^[a-zA-Z0-9]*$";
		if(s.matches(pattern)) {
			return true;
		}
		return false;
	}

    public static void showDialog(String msg, Context context) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    context).create();

            // Setting Dialog Title
            alertDialog.setTitle("Alert !");

            // Setting Dialog Message
            alertDialog.setMessage(msg);

            // Setting Icon to Dialog
            //	alertDialog.setIcon(R.drawable.browse);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	// Convert date String from one format to another

	public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
		SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

		try {
			parsed = df_input.parse(inputDate);
			outputDate = df_output.format(parsed);

		} catch (ParseException e) {
			Log.e("TAG Date", "ParseException - dateFormat");
		}

		return outputDate;

	}

}
