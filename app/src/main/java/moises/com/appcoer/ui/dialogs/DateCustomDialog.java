package moises.com.appcoer.ui.dialogs;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.tools.Utils;

public class DateCustomDialog extends DatePickerDialog implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = DateCustomDialog.class.getSimpleName();
    private static final String ARG_PARAM_1 = "reserveDate";
    private static final String ARG_PARAM_2 = "textDates";
    private OnDateCustomDialogListener mListener;
    private ReserveDate reserveDate;
    private String[] textDates;

    public DateCustomDialog(){}

    public static DateCustomDialog newInstance(OnDateCustomDialogListener listener, ReserveDate reserveDate, String[] textDates){
        DateCustomDialog dateDialog = new DateCustomDialog();
        dateDialog.setOnDateCustomDialogListener(listener);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM_1, reserveDate);
        bundle.putStringArray(ARG_PARAM_2, textDates);
        dateDialog.setArguments(bundle);
        return dateDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            reserveDate = (ReserveDate) getArguments().getSerializable(ARG_PARAM_1);
            textDates = getArguments().getStringArray(ARG_PARAM_2);
        }
        setVersion(Version.VERSION_1);
        setCancelColor(Utils.getColor(R.color.divider));
        setOnDateSetListener(this);
        if(reserveDate != null)
            setTitle(reserveDate == ReserveDate.FROM_DATE ? getString(R.string.from_date) : getString(R.string.to_date));
    }

    @Override
    public void onStart() {
        super.onStart();
        setMinDate(Utils.toCalendar(new Date()));
        if(textDates != null)
            disabledDays(Arrays.asList(textDates));
    }

    private void disabledDays(List<String> dateTextList){
        List<Date> dateList = new ArrayList<>();
        for (String dateText: dateTextList)
            dateList.add(Utils.parseStringToDate(dateText, Utils.DATE_FORMAT_INPUT));

        if(dateList.size() > 0){
            Calendar[] calendars = new Calendar[dateList.size()];
            int i = 0;
            for (Date date: dateList) {
                calendars[i] = Utils.toCalendar(date);
                i++;
            }
            setDisabledDays(calendars);
            updated();
        }
    }

    private void updated(){
        try{
            final Calendar c = Calendar.getInstance();
            int year1 = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            onDayOfMonthSelected(year1, month, day);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnDateCustomDialogListener(OnDateCustomDialogListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Date date = calendar.getTime();

        if(mListener != null)
            mListener.onDateSelected(Utils.getCustomizedDate(Utils.DATE_FORMAT_SHORT, date), date, reserveDate);
    }

    public interface OnDateCustomDialogListener{
        void onDateSelected(String dateText, Date date, ReserveDate reserveDate);
    }

    public enum ReserveDate {
        FROM_DATE, TO_DATE
    }
}
