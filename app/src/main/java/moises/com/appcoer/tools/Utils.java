package moises.com.appcoer.tools;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import moises.com.appcoer.R;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.ui.App;

public class Utils {

    //DATE FORMAT
    public static final String DATE_FORMAT_INPUT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SHORT = "dd-MM-yy";
    public static final String DATE_FORMAT_INPUT_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "EEEE d MMM, yyyy";
    public static final String DATE_FORMAT_DAY_DETAILED = "MMMM dd, yyyy";
    public static final String DATE_FORMAT_TIME = "HH:mm:ss";
    public static final String DATE_FORMAT_TIME_12 = "hh:mm a";
    public static final String DATE_FORMAT_TIME_24 = "HH:mm";
    public static final String DATE_FORMAT_CUSTOM_1 = "EEE d MMM yyyy HH:mm";

    //LANGUAGE
    public static final String LANGUAGE_SPANISH = "es";
    public static final String LANGUAGE_ENGLISH = "en";

    public static String getCurrentLanguage() {
        String locale = Locale.getDefault().getLanguage();
        if(locale.equals(LANGUAGE_SPANISH)){
            return LANGUAGE_SPANISH;
        }

        return LANGUAGE_ENGLISH;
    }

    public static void showToastMessage(String message){
        Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showDialogMessage(String title, String message, DialogInterface.OnClickListener listener){
        AlertDialog.Builder dialog = new AlertDialog.Builder(GlobalManager.getActivityGlobal());
        if(!title.isEmpty())
            dialog.setTitle(title);
        dialog.setMessage(message);
        if(listener == null){
            dialog.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else{
            dialog.setNeutralButton(android.R.string.ok, listener);
        }
        dialog.create().show();
    }

    public static void showDialogMessageWithAction(String title, String message, String titlePositiveButton, DialogInterface.OnClickListener listener){
        AlertDialog.Builder dialog = new AlertDialog.Builder(GlobalManager.getActivityGlobal());
        if(!title.isEmpty())
            dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton(titlePositiveButton, listener);
        dialog.create().show();
    }

    public static void showSnackMessage(String message){
        Snackbar.make(GlobalManager.getActivityGlobal().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    public void showMessageWithAction(String message, String titleAction, View.OnClickListener onClickListener){
        Snackbar.make(GlobalManager.getActivityGlobal().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(GlobalManager.getActivityGlobal(), R.color.colorPrimary))
                .setAction(titleAction, onClickListener)
                .show();
    }

    public static int getColor(int id){
        //return ContextCompat.getColor(GlobalManager.getActivityGlobal(), id);
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(GlobalManager.getActivityGlobal(), id);
        } else {
            return GlobalManager.getActivityGlobal().getResources().getColor(id);
        }
    }

    public static Date getCurrentDate(){
        return new Date();
    }

    //----------------------METHODS FOR DATE-----------------
    public static String getCustomDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_DAY_DETAILED, Locale.getDefault());
        long difference = getDifferenceDays(getCurrentDate(), date);

        String text = formatter.format(date);
        if (difference == 0){
            return GlobalManager.getActivityGlobal().getString(R.string.today) + " "+ text;
        }
        return text;
    }

    public static long getDifferenceDays(Date currentDate, Date date){
        return (currentDate.getTime() - date.getTime())/(1000*60*60*24);
    }

    public static String getCustomizedDate(String format, Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(date);
    }

    public static Date parseStringToDate(String dateString, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date();

        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void hideKeyboard(View view){
        if(view != null){
            InputMethodManager imm = (InputMethodManager)GlobalManager.getActivityGlobal().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static List<Date> getDaysBetweenDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }
}
