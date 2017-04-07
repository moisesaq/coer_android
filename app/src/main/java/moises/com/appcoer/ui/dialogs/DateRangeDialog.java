package moises.com.appcoer.ui.dialogs;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.tools.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateRangeDialog extends DatePickerDialog implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = DateRangeDialog.class.getSimpleName();
    private OnDateRangeDialogListener mListener;
    private int idRoom;

    public DateRangeDialog(){}

    public static DateRangeDialog newInstance(OnDateRangeDialogListener listener, int idRoom){
        DateRangeDialog dateDialog = new DateRangeDialog();
        dateDialog.setOnDateRangeDialogListener(listener);
        Bundle bundle = new Bundle();
        bundle.putInt("idRoom", idRoom);
        dateDialog.setArguments(bundle);
        return dateDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            idRoom = getArguments().getInt("idRoom");
    }

    /*@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreateDialog(savedInstanceState);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, year, month, day-1);
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog.getDialog();
    }*/

    @Override
    public void initialize(OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        int year1 = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        super.initialize(callBack, year1, month, day-1);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadBusyDates();
    }

    private void loadBusyDates(){
        if(idRoom == 0)
            return;
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<String>> listCall = apiClient.getRoomBusyDate(idRoom, Session.getInstance().getUser().getApiToken());
        listCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0){
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                }else{
                    Log.d(TAG, " SUCCESS >>> EMPTY");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d(TAG, " ERROR >>> " + t.toString());
            }
        });
    }

    /*@Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        Date date = calendar.getTime();

        datePicker.setSelected(false);

        if(onDateDialogListener != null)
            onDateDialogListener.onDateSelected(Utils.getCustomizedDate(Utils.DATE_FORMAT_INPUT, date), date);
    }*/

    public void setOnDateRangeDialogListener(OnDateRangeDialogListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

    }

    public interface OnDateRangeDialogListener{
        void onDateRangeSelected();
    }
}
