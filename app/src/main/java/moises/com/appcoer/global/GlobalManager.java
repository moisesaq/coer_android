package moises.com.appcoer.global;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import moises.com.appcoer.R;

@Deprecated
public class GlobalManager {

    private static AppCompatActivity activityGlobal;
    private static ProgressDialog progressDialog;

    private GlobalManager() {
    }

    public static void setActivityGlobal(AppCompatActivity activity) {
        activityGlobal = activity;
    }

    public static AppCompatActivity getActivityGlobal() {
        return activityGlobal;
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
