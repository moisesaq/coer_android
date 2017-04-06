package moises.com.appcoer.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.API;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.Lodging;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.model.Room;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.adapters.SpinnerRoomsAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.dialogs.AmountPeopleDialog;
import moises.com.appcoer.ui.dialogs.DateDialog;
import moises.com.appcoer.ui.view.InputTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveRoomFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener,
                                                                        DateDialog.OnDateDialogListener, AmountPeopleDialog.OnAmountPeopleDialogListener {
    private static final String TAG = ReserveRoomFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "lodging";

    private Lodging mLodging;

    private Spinner spinnerRooms;
    private SpinnerRoomsAdapter mSpinnerRoomsAdapter;
    private InputTextView mDates, mName, mLastName, mEmail, mPhone, mAmountPeople, mAdditionalInformation;
    private Room mRoom;
    private List<String> dateList;

    public ReserveRoomFragment() {
    }

    public static ReserveRoomFragment newInstance(Lodging lodging) {
        ReserveRoomFragment fragment = new ReserveRoomFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, lodging);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLodging = (Lodging) getArguments().getSerializable(ARG_PARAM1);
        }
        dateList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve_room, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        setTitle(getString(R.string.title_reserve_room));
        spinnerRooms = (Spinner)view.findViewById(R.id.spinner_rooms);
        spinnerRooms.setOnItemSelectedListener(this);
        List<Room> list = new ArrayList<>();
        list.add(getDefaultRoom());
        mSpinnerRoomsAdapter = new SpinnerRoomsAdapter(getContext(), list);
        spinnerRooms.setAdapter(mSpinnerRoomsAdapter);

        mDates = (InputTextView)view.findViewById(R.id.itv_dates);
        mDates.setOnClickListener(this);
        mName = (InputTextView)view.findViewById(R.id.itv_name);
        mLastName = (InputTextView)view.findViewById(R.id.itv_last_name);
        mEmail = (InputTextView)view.findViewById(R.id.itv_email);
        mPhone = (InputTextView)view.findViewById(R.id.itv_phone);
        mAmountPeople = (InputTextView)view.findViewById(R.id.itv_amount_people);
        mAmountPeople.setOnClickListener(this);
        mAdditionalInformation = (InputTextView)view.findViewById(R.id.itv_additional_information);

        Button mConfirm = (Button)view.findViewById(R.id.b_confirm);
        mConfirm.setOnClickListener(this);
        getRooms();
    }

    private Room getDefaultRoom(){
        Room room = new Room();
        room.setId(100);
        room.setRoomText(getString(R.string.select_room));
        return room;
    }

    private void getRooms(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        //String urlLodging = String.format("%s%s%s%s", API.LODGINGS, "/", mLodging.getId(), API.ROOM_LIST);
        Call<List<Room>> listCall = apiClient.getRoomList(mLodging.getId(), Session.getInstance().getUser().getApiToken());
        listCall.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                Log.d(TAG, " Success >>>> " + response.body().toString());
                if(response.body() != null && response.body().size() > 0){
                    mSpinnerRoomsAdapter.addAll(response.body());

                }else {
                    Log.d(TAG, " >>>> Error");
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.d(TAG, "Error ?>>> " + t.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.itv_dates:
                new DateDialog(this).show(getFragmentManager(), DateDialog.TAG);
                break;
            case R.id.itv_amount_people:
                AmountPeopleDialog.newInstance(1, 4, this).show(getFragmentManager(), AmountPeopleDialog.TAG);
                break;
            case R.id.b_confirm:
                if(mDates.isTextValid() && mName.isTextValid() && mLastName.isTextValid()
                            && mEmail.isEmailValid() && mPhone.isPhoneValid() && mAmountPeople.isTextValid() && mAdditionalInformation.isTextValid()){
                    reserveRoom(createReservation());
                }
                break;
        }
    }

    private void reserveRoom(final Reservation reservation){
        if(mRoom == null || mRoom.getId() == 0)
            return;
        Log.d(TAG, " Reservation >>>> " + reservation.toString());
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<Reservation> reservationCall = apiClient.reserveRoom(mRoom.getId(), Session.getInstance().getUser().getApiToken(), reservation);
        reservationCall.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                Log.d(TAG, " Success >>>> " + response.message() + "code " +response.code());
                if(response.isSuccessful()){
                    Log.d(TAG, " Success >>>> " + response.body().toString());
                    Utils.showDialogMessage("", getString(R.string.message_reservation_successful), null);
                }else {
                    Utils.showDialogMessage("", getString(R.string.message_reservation_failed), null);
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
            }
        });
    }

    private Reservation createReservation(){
        Reservation reservation = new Reservation();
        //reservation.setDates(dateList);
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
    public void onDateSelected(String textDate, Date date) {
        if(mDates.getText().isEmpty()){
            mDates.setText(textDate);
        }else {
            mDates.setText(String.format("%s%s%s", mDates.getText(), ",", textDate));
        }
        dateList.add(textDate);
    }

    @Override
    public void onNumberPeopleSelected(int numberPeople) {
        mAmountPeople.setText(String.valueOf(numberPeople));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mRoom = (Room) adapterView.getAdapter().getItem(i);
        Log.d(TAG, " >>>> ID ROOM >>> " + mRoom.getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public interface OnReserveRoomFragmentListener {
        void onReserveRoomSuccessful();
    }
}
