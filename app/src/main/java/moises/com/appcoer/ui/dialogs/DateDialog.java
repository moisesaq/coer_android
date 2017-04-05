package moises.com.appcoer.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import moises.com.appcoer.R;
import moises.com.appcoer.tools.Utils;

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "DATE_DIALOG";
    private OnDateDialogListener onDateDialogListener;

    public DateDialog(){}

    @SuppressLint("ValidFragment")
    public DateDialog(OnDateDialogListener listener){
        this.onDateDialogListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        Date date = calendar.getTime();

        datePicker.setSelected(false);

        if(onDateDialogListener != null)
            onDateDialogListener.onDateSelected(Utils.getCustomizedDate(Utils.DATE_FORMAT_INPUT, date), date);
    }

    public interface OnDateDialogListener{
        void onDateSelected(String textDate, Date date);
    }
}
