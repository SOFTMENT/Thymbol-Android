package com.softment.thymbol.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softment.thymbol.MainActivity;
import com.softment.thymbol.R;
import com.softment.thymbol.SignInActivity;
import com.softment.thymbol.SignUpActivity;
import com.softment.thymbol.Model.UserModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Services {




    public static  String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
    public static boolean isPromoting(Date date){
        Date currentDate = new Date();
        if (currentDate.compareTo(date) < 0) {
            return true;
        }
        else {
            return false;
        }
    }



    public static Date convertTimeToDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        return cal.getTime();
    }

    public static void sentPushNotificationToAdmin(Context context,String title, String message) {
        final String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + "AAAAqBzSPAk:APA91bEGVz80gPuqdqNp0CzMZ5tdmRui4XFfSj6bPDnQ9AStQM-NhkRHeCNXrx8USkxdb97U9BLWv8U2Ri1UITA-TID3RPc4BXYne5nmF3GeB9p4XkbiIVjIQdo7G7alXkBK9hpEAI5T";
        final String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);
            notification.put("to", "/topics/admin");
            notification.put("data", notifcationBody);
        } catch (JSONException ignored) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }

    public static void sentPushNotification(Context context,String title, String message, String token) {
        final String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + "AAAAqBzSPAk:APA91bEGVz80gPuqdqNp0CzMZ5tdmRui4XFfSj6bPDnQ9AStQM-NhkRHeCNXrx8USkxdb97U9BLWv8U2Ri1UITA-TID3RPc4BXYne5nmF3GeB9p4XkbiIVjIQdo7G7alXkBK9hpEAI5T";
        final String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);
            notification.put("to", token);
            notification.put("data", notifcationBody);
        } catch (JSONException ignored) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }
    public static  String convertDateForReview(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd MMM";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }
    public static  String convertDateToString(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd-MMM-yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }
    public static  String convertDateToMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "MMMM";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }

    public static  String convertDateToStringWithoutDash(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd MMM yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }

    public static  String convertDateToTimeString(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd-MMM-yyyy, hh:mm a";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }

    public static void showCenterToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
    public static String getTimeAgo(Context context,Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return context.getString(R.string.future);
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.momentsago);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return context.getString(R.string.aminuteago);
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + context.getString(R.string.minutesago);
        } else if (diff < 2 * HOUR_MILLIS) {
            return context.getString(R.string.anhourago);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + context.getString(R.string.hoursago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.yesterday);
        } else {
            return diff / DAY_MILLIS + context.getString(R.string.daysago);
        }
    }

    public static void logout(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(R.string.logout);
        builder.setMessage(R.string.areyousureyouwantologout);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }


    public static String toUpperCase(String str) {
        if (str.isEmpty()){
            return "";
        }
        String[] names = str.trim().split(" ");
        str = "";
        for (String name : names) {
            try {
                str += name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + " ";
            }
            catch (Exception ignored){

            }
        }
     return str;
    }
    public static void showDialog(Context context,String title,String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        Activity activity = (Activity) context;
        View view = activity.getLayoutInflater().inflate(R.layout.error_message_layout, null);
        TextView titleView = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        titleView.setText(title);
        msg.setText(message);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (title.equalsIgnoreCase(context.getString(R.string.VERIFYYOUREMAIL))) {
                    if (context instanceof SignUpActivity) {
                        Intent intent = new Intent(context, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                }


            }
        });

        if(!((Activity) context).isFinishing())
        {
            alertDialog.show();

        }

    }





    public static void addUserDataOnServer(Context context,UserModel userModel){

        ProgressHud.show(context,"");
        FirebaseFirestore.getInstance().collection("Users").document(userModel.getUid()).set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    if (userModel.regiType.equalsIgnoreCase("custom")) {
                        Services.getCurrentUserData(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                    }
                    else {
                        Services.getCurrentUserData(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                    }

                }
                else {
                    Services.showDialog(context,"ERROR",task.getException().getLocalizedMessage());
                }
            }
        });
    }



    public static void getCurrentUserData(Context context,String uid, Boolean showProgress) {

        if (showProgress) {
            ProgressHud.show(context,"");
        }

        FirebaseFirestore.getInstance().collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if (showProgress) {
                    ProgressHud.dialog.dismiss();
                }

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        documentSnapshot.toObject(UserModel.class);

                        if (UserModel.data != null) {

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);


                        }
                        else  {
                           showCenterToast(context,"Something Went Wrong. Code - 101");
                        }
                    }
                    else {
                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showDialog(context,context.getString(R.string.usernotfound),context.getString(R.string.youraccountisnotavailable));
                                }
                                else {
                                    showDialog(context,context.getString(R.string.ERROR),task.getException().getLocalizedMessage());
                                }
                            }
                        });
                    }
                }
                else {
                    Services.showDialog(context,context.getString(R.string.ERROR),task.getException().getMessage());
                }


            }
        });
    }

    public  static Date getDateFromTimestamp(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


}
