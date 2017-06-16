package moises.com.appcoer.ui.home.reserve;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.LogEvent;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.model.Room;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.adapters.SpinnerRoomsAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.dialogs.AmountPeopleDialog;
import moises.com.appcoer.ui.dialogs.DateCustomDialog;
import moises.com.appcoer.ui.view.InputTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveRoomFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener,
                                                                        AmountPeopleDialog.OnAmountPeopleDialogListener, InputTextView.Callback,
                                                                                    DateCustomDialog.OnDateCustomDialogListener, TextView.OnEditorActionListener{
    public static final int ID_PARANA = 1;
    public static final int ID_TIMBUES = 2;
    private static final String TAG = ReserveRoomFragment.class.getSimpleName();
    private static final int ID_ROOM_DEFAULT = 1010;
    private static final String ARG_PARAM1 = "idLodging";

    private int idLodging;
    private ProgressBar mProgressBar;
    private SpinnerRoomsAdapter mSpinnerRoomsAdapter;
    private InputTextView mFromDate, mToDate, mName, mLastName, mEmail, mPhone, mAmountPeople, mAdditionalInformation;
    private Room mRoom;
    private List<String> dateListForReserve;
    private String[] textDatesNotAvailable;
    private Date fromDate, toDate;

    public ReserveRoomFragment() {
    }

    public static ReserveRoomFragment newInstance(int idLodging) {
        ReserveRoomFragment fragment = new ReserveRoomFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, idLodging);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            idLodging = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve_room, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        setTitle(getString(R.string.title_reserve_room));
        TextView mTitleLodging = (TextView)view.findViewById(R.id.tv_title_lodging);
        if(idLodging == ID_PARANA)
            mTitleLodging.setText(getString(R.string.lodging_panana));
        Spinner spinnerRooms = (Spinner)view.findViewById(R.id.spinner_rooms);
        spinnerRooms.setOnItemSelectedListener(this);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        List<Room> list = new ArrayList<>();
        list.add(getDefaultRoom());
        mSpinnerRoomsAdapter = new SpinnerRoomsAdapter(getContext(), list);
        spinnerRooms.setAdapter(mSpinnerRoomsAdapter);

        mFromDate = (InputTextView)view.findViewById(R.id.itv_from_date);
        mFromDate.setEnabled(false);
        mFromDate.addCallback(this);
        mToDate = (InputTextView)view.findViewById(R.id.itv_to_date);
        mToDate.setEnabled(false);
        mToDate.addCallback(this);

        mName = (InputTextView)view.findViewById(R.id.itv_name);
        mName.getEditText().setOnEditorActionListener(this);

        mLastName = (InputTextView)view.findViewById(R.id.itv_last_name);
        mLastName.getEditText().setOnEditorActionListener(this);
        mEmail = (InputTextView)view.findViewById(R.id.itv_email);
        mEmail.getEditText().setOnEditorActionListener(this);
        mPhone = (InputTextView)view.findViewById(R.id.itv_phone);
        mPhone.getEditText().setOnEditorActionListener(this);

        mAmountPeople = (InputTextView)view.findViewById(R.id.itv_amount_people);
        mAmountPeople.setEnabled(false);
        mAmountPeople.addCallback(this);
        mAdditionalInformation = (InputTextView)view.findViewById(R.id.itv_additional_information);

        Button mConfirm = (Button)view.findViewById(R.id.b_confirm);
        mConfirm.setOnClickListener(this);
        getRooms();
    }

    private Room getDefaultRoom(){
        Room room = new Room();
        room.setId(ID_ROOM_DEFAULT);
        room.setRoomText(getString(R.string.select_room));
        return room;
    }

    private void getRooms(){
        mProgressBar.setVisibility(View.VISIBLE);
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<Room>> listCall = apiClient.getRoomList(idLodging, Session.getInstance().getUser().getApiToken());
        listCall.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                Log.d(TAG, " Success >>>> " + response.body().toString());
                if(response.body() != null && response.body().size() > 0){
                    mSpinnerRoomsAdapter.addAll(response.body());
                }else {
                    Log.d(TAG, " >>>> Error");
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.d(TAG, "Error ?>>> " + t.toString());
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActionIconClick(View view) {
        switch (view.getId()){
            case R.id.itv_from_date:
                showDateDialog(DateCustomDialog.ReserveDate.FROM_DATE);
                break;
            case R.id.itv_to_date:
                showDateDialog(DateCustomDialog.ReserveDate.TO_DATE);
                break;
            case R.id.itv_amount_people:
                AmountPeopleDialog.newInstance(1, 4, this).show(getFragmentManager(), AmountPeopleDialog.TAG);
                break;
        }
    }

    private void showDateDialog(DateCustomDialog.ReserveDate reserveDate){
        if(mRoom.getId() != ID_ROOM_DEFAULT && textDatesNotAvailable != null){
            DateCustomDialog.newInstance(this, reserveDate, textDatesNotAvailable).show(getActivity().getFragmentManager(), DateCustomDialog.TAG);
        }else {
            Utils.showToastMessage(getString(R.string.select_room));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_confirm:
                if(mFromDate.isTextValid() && mToDate.isTextValid() && mName.isTextValid() && mLastName.isTextValid()
                            && mEmail.isEmailValid() && mPhone.isPhoneValid() && mAmountPeople.isTextValid() && mAdditionalInformation.isTextValid()){
                    reserveRoom(createReservation());
                }
                break;
        }
    }

    private void reserveRoom(final Reservation reservation){
        if(mRoom == null || mRoom.getId() == 1010){
            Utils.showToastMessage(getString(R.string.select_room));
            return;
        }
        Log.d(TAG, " Reservation >>>> " + reservation.toString());
        GlobalManager.showProgressDialog();
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<Reservation> reservationCall = apiClient.reserveRoom(mRoom.getId(), Session.getInstance().getUser().getApiToken(), reservation);
        reservationCall.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                Log.d(TAG, " Success >>>> " + response.message() + "code " +response.code());
                GlobalManager.dismissProgressDialog();
                if(response.isSuccessful()){
                    LogEvent.logEventFirebaseAnalytic(getContext(), LogEvent.EVENT_RESERVE_ROON);
                    Utils.showDialogMessage("", getString(R.string.message_reservation_successful), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    });
                }else {
                    Utils.showDialogMessage("", getSafeString(R.string.message_reservation_failed), null);
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                GlobalManager.dismissProgressDialog();
                Utils.showDialogMessage("", getSafeString(R.string.message_something_went_wrong), null);
            }
        });
    }

    private Reservation createReservation(){
        Reservation reservation = new Reservation();
        reservation.setDates(dateListForReserve);
        //reservation.setMp(Session.getInstance().getUser().getMp());
        reservation.setName(mName.getText());
        reservation.setLastName(mLastName.getText());
        reservation.setEmail(mEmail.getText());
        reservation.setPhone(mPhone.getText());
        reservation.setAmountPeople(Integer.parseInt(mAmountPeople.getText()));
        reservation.setDetail(mAdditionalInformation.getText());
        return reservation;
    }

    @Override
    public void onDateSelected(String textDate, Date date, DateCustomDialog.ReserveDate reserveDate) {
        if(reserveDate == DateCustomDialog.ReserveDate.FROM_DATE){
            mFromDate.setText(textDate);
            fromDate = date;
        }else {
            mToDate.setText(textDate);
            toDate = date;
        }
        prepareReserveDates();
    }

    private void prepareReserveDates(){
        if(fromDate != null && toDate != null && fromDate.before(toDate) && textDatesNotAvailable != null){
            List<String> dateListSelected = getDatesString(Utils.getDaysBetweenDates(fromDate, toDate));
            List<String> dateListNotAvailable = Arrays.asList(textDatesNotAvailable);
            if(isDatesSelectedAvailable(dateListSelected, dateListNotAvailable)){
                dateListForReserve = dateListSelected;
            }else{
                Utils.showDialogMessage("", getSafeString(R.string.message_error_dates_selected), null);
                clearDates();
            }
        }
    }

    private boolean isDatesSelectedAvailable(List<String> dateListSelected, List<String> dateListNotAvailable){
        int count = dateListSelected.size();
        dateListSelected.removeAll(dateListNotAvailable);
        return count == dateListSelected.size();
    }

    @Override
    public void onNumberPeopleSelected(int numberPeople) {
        mAmountPeople.setText(String.valueOf(numberPeople));
        mAdditionalInformation.requestFocus();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        clearDates();
        textDatesNotAvailable = null;
        mRoom = (Room) adapterView.getAdapter().getItem(i);
        if(mRoom.getId() != ID_ROOM_DEFAULT)
            loadBusyDates(mRoom);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void loadBusyDates(Room room){
        mProgressBar.setVisibility(View.VISIBLE);
        Utils.showToastMessage(getString(R.string.updating_dates));
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<String>> listCall = apiClient.getRoomBusyDate(room.getId(), Session.getInstance().getUser().getApiToken());
        listCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                Log.d(TAG, " RESULT >>> " + response.code() + " --> " + response.message());
                if(response.isSuccessful() && response.body() != null){
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                    textDatesNotAvailable = new String[response.body().size()];
                    textDatesNotAvailable = response.body().toArray(textDatesNotAvailable);
                    showDateDialog(DateCustomDialog.ReserveDate.FROM_DATE);
                }else{
                    Utils.showToastMessage(getSafeString(R.string.message_something_went_wrong));
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Utils.showToastMessage(getSafeString(R.string.message_something_went_wrong));
            }
        });
    }

    private void clearDates(){
        mFromDate.clearField();
        fromDate = null;
        mToDate.clearField();
        toDate = null;
    }

    public static List<String> getDatesString(List<Date> dateList){
        List<String> stringList = new ArrayList<>();
        for (Date date: dateList){
            stringList.add(Utils.getCustomizedDate(Utils.DATE_FORMAT_INPUT, date));
        }
        return stringList;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT){
            if(mName.hasFocus()){
                mLastName.requestFocus();
            }else if(mEmail.hasFocus()){
                mPhone.requestFocus();
            }else if(mPhone.hasFocus()){
                AmountPeopleDialog.newInstance(1, 4, this).show(getFragmentManager(), AmountPeopleDialog.TAG);
            }
        }
        return false;
    }

    public interface OnReserveRoomFragmentListener {
        void onReserveRoomSuccessful();
    }
}
