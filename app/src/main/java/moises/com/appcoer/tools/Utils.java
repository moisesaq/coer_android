package moises.com.appcoer.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
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
import moises.com.appcoer.ui.CoerApplication;

@Deprecated
public class Utils {
    //DATE FORMAT
    public static final String DATE_FORMAT_INPUT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SHORT = "dd-MM-yy";
    public static final String DATE_FORMAT_INPUT_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "EEEE d MMM, yyyy";
    private static final String DATE_FORMAT_DAY_DETAILED = "MMMM dd, yyyy";

    public static void showToastMessage(String message){
        Toast.makeText(CoerApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showDialogMessage(String title, String message, DialogInterface.OnClickListener listener){
        AlertDialog.Builder dialog = new AlertDialog.Builder(GlobalManager.getActivityGlobal());
        if(!title.isEmpty())
            dialog.setTitle(title);
        dialog.setMessage(message);
        if(listener == null){
            dialog.setNeutralButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss());
        }else{
            dialog.setNeutralButton(android.R.string.ok, listener);
        }
        dialog.create().show();
    }

    public static int getColor(int id){
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(GlobalManager.getActivityGlobal(), id);
        } else {
            return GlobalManager.getActivityGlobal().getResources().getColor(id);
        }
    }

    private static Date getCurrentDate(){
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

    private static long getDifferenceDays(Date currentDate, Date date){
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

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_COMPACT);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
