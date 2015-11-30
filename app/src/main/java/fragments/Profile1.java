package fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.headsup.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import Functions.Constants;

/**
 * Created by sandeep on 27/10/15.
 */
public class Profile1 extends Fragment {

    ImageView profile_pic;
    ImageView edit;
    static EditText bio;
    TextView user_name;

    File imgFileGallery;

    public static ContentResolver appContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_1, container, false);

        appContext = getActivity().getContentResolver();

        bio = (EditText) rootView.findViewById(R.id.bio);
        profile_pic = (ImageView) rootView.findViewById(R.id.profile_pic);
        edit = (ImageView) rootView.findViewById(R.id.edit);
        user_name = (TextView) rootView.findViewById(R.id.user_name);

        if(Constants.IS_COMING_FROM_FB_GOOGLE){
            edit.setVisibility(View.GONE);
        }else {
            edit.setVisibility(View.VISIBLE);
        }


        user_name.setText(Constants.USERNAME_REGISTER);

        if(Constants.IS_COMING_FROM_FB_GOOGLE){
            new LoadProfileImage(profile_pic).execute(Constants.FB_GOOGLE_PRO_PIC_URL);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GaleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(GaleryIntent, 1);
            }
        });

        return rootView;
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            Constants.BITMAP_TO_SEND = result ;
            
            ConvertBitmapToFile(result);
        }
    }

    private void ConvertBitmapToFile(Bitmap result) {
        try {
            imgFileGallery = new File(getActivity().getCacheDir(), "profile_pic.png");
            imgFileGallery.createNewFile();

            //Convert bitmap to byte array

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(imgFileGallery);
            fos.write(bitmapdata);

            Constants.FILE_TO_SEND = imgFileGallery;
            fos.flush();
            fos.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    public static Boolean checkDetailIsFilledOrNot() {
        String boi_text = bio.getText().toString();
        if(boi_text.trim().length()<1){
            bio.setError("Please enter bio.");
            return false ;
        } else {
            Constants.BIO = boi_text;
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();

                InputStream imageStream = null;
                try {
                    imageStream = appContext.openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("Exception==", "" + e);
                }
              Bitmap  takenImage = BitmapFactory.decodeStream(imageStream);



                profile_pic.setImageBitmap(takenImage);

                /**
                 * saving to file
                 */

                Uri SelectedImage = data.getData();
                String[] FilePathColumn = {MediaStore.Images.Media.DATA};

                Cursor SelectedCursor = appContext.query(SelectedImage,
                        FilePathColumn, null, null, null);
                SelectedCursor.moveToFirst();

                int columnIndex = SelectedCursor
                        .getColumnIndex(FilePathColumn[0]);
                String picturePath = SelectedCursor.getString(columnIndex);
                SelectedCursor.close();

                Log.e("picturePath==", "" + picturePath);

                imgFileGallery = new File(picturePath);

                Constants.FILE_TO_SEND = imgFileGallery ;


            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
