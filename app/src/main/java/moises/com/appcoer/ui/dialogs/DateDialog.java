package moises.com.appcoer.ui.dialogs;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiService;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.tools.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = DateDialog.class.getSimpleName();
    private OnDateDialogListener onDateDialogListener;
    private int idRoom;

    public DateDialog(){}

    public static DateDialog newInstance(OnDateDialogListener listener, int idRoom){
        DateDialog dateDialog = new DateDialog();
        dateDialog.setOnDateDialogListener(listener);
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
    public void onStart() {
        super.onStart();
        loadBusyDates();
    }

    private void loadBusyDates(){
        if(idRoom == 0)
            return;
        ApiService apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<String>> listCall = apiClient.getRoomBusyDate(idRoom,
                SessionManager.getInstance(getActivity()).getUser().getApiToken());
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

    public void setOnDateDialogListener(OnDateDialogListener onDateDialogListener) {
        this.onDateDialogListener = onDateDialogListener;
    }

    public interface OnDateDialogListener{
        void onDateSelected(String textDate, Date date);
    }
}
