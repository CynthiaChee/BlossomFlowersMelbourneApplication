package com.example.mysearchingapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.ByteArrayOutputStream;

public class Util {

    public static final int PICK_DESTINATION_REQUEST = 300;
    public static final String DESTINATION_LATITUDE = "destination_latitude";
    public static final String DESTINATION_LONGITUDE = "destination_longitude";
    public static final int DATABASE_VERSION = 1;
    public static final String LOGGEDIN_USER = "loggedin_user";
    public static final String SHARED_PREF_DATA = "shared_pref_data";

    public static final String USER_DATABASE_NAME = "user_db";
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_ID = "user_id";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PHONE_NO = "phone_no";
    public static final String ORDER_DATABASE_NAME = "order_db";
    public static final String ORDER_TABLE_NAME = "orders";
    public static final String ORDER_ID = "order_id";
    public static final String FLOWER_IMAGE = "flower_image";
    public static final String RECEIVER_NAME = "receiver";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String LOCATION = "location";
    public static final String DESTINATION = "destination";
    public static final String FLOWER_TYPE = "flower_type";
    public static final String QUANTITY = "quantity";
    public static final String MSG = "message";

    /**
    * function to create a toast
    * @param context activity for where this function is called
    * @param message message to be shown on toast
    */
    public static void createToast(Context context, String message) {
       Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


   /**
    * convert from Bitmap to byte array
    * @param bitmap bitmap to obtain bytes array from
    * @return bytes array from bitmap
    */
    public static byte[] getBytesArrayFromBitmap(Bitmap bitmap) {
       ByteArrayOutputStream stream = new ByteArrayOutputStream();
       bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
       return stream.toByteArray();
    }

    /**
    * convert from byte array to Bitmap
    * @param image image byte array to decode bitmap from
    * @return bitmap from the input byte array
    */
    public static Bitmap getBitmapFromBytesArray(byte[] image) {
       return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

   /**
    * convert from drawable resource to bitmap
    * @param context activity for where this function is called
    * @param drawable drawable resource ID
    * @return bitmap decoded from drawable resource
    */
    public static Bitmap getBitmapFromDrawable(Context context, int drawable) {
       Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
       return bitmap;
    }

}
