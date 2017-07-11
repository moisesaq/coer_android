package moises.com.appcoer.global;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;

import java.util.regex.Pattern;

import moises.com.appcoer.R;
import moises.com.appcoer.ui.CoerApplication;

public class GlobalManager {

    private static AppCompatActivity activityGlobal;
    private static ProgressDialog progressDialog;

    private static Ringtone ringtone;
    private static Vibrator vibrator;

    private GlobalManager() {
    }

    public static void setActivityGlobal(AppCompatActivity activity) {
        activityGlobal = activity;
        initializeRingtone();
    }

    public static AppCompatActivity getActivityGlobal() {
        return activityGlobal;
    }

    /*HAS PERMISSION*/
    public static boolean hasPermission(String[] permissions){
        int cont = 0;
        for (String permission: permissions) {
            if(ContextCompat.checkSelfPermission(GlobalManager.getActivityGlobal(), permission) == PackageManager.PERMISSION_GRANTED)
                cont++;
        }
        return cont == permissions.length;
    }

    public static boolean resultPermission(int[] grantResults){
        int cont = 0;
        for(int i = 0; i < grantResults.length; i++){
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED )
                cont++;
        }
        return cont == grantResults.length;
    }

    public String getOwnerEmail() {
        String possibleEmail = null;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        if (ActivityCompat.checkSelfPermission(CoerApplication.getContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }

        Account[] accounts = AccountManager.get(activityGlobal).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches())
                possibleEmail = account.name;
        }
        return possibleEmail;
    }

    private static void initializeRingtone(){
        if(ringtone == null){
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ringtone = RingtoneManager.getRingtone(activityGlobal, notification);
            vibrator = (Vibrator)activityGlobal.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public static void playRingtone(){
        if(ringtone.isPlaying())
            ringtone.stop();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activityGlobal);
        int typeSound = Integer.parseInt(sharedPreferences.getString("pref_sound", "1"));
        switch (typeSound){
            case 1:
                ringtone.play();
                break;
            case 2:
                vibrator.vibrate(1000);
                break;
            case 3:
                ringtone.play();
                vibrator.vibrate(1000);
                break;
            case 4:
                break;
        }
    }

    public static void showProgressDialog(){
        showProgressDialog(activityGlobal.getString(R.string.loading));
    }

    public static void showProgressDialog(String message){
        progressDialog = new ProgressDialog(activityGlobal);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void editMessageProgressDialog(String newMessage){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.setMessage(newMessage);
        }
    }

    public static void dismissProgressDialog(){
        if(progressDialog != null)
            progressDialog.dismiss();
    }

    public static CustomTabsIntent getCustomTabsIntent(Activity activity){
        CustomTabsIntent.Builder builder =  new CustomTabsIntent.Builder()
                .setStartAnimations(activity, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .setExitAnimations(activity, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
                .setSecondaryToolbarColor(ContextCompat.getColor(activity, R.color.colorAccent))
                .setShowTitle(true)
                .setCloseButtonIcon(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_window_close_white_24dp));
        return builder.build();
    }
}
