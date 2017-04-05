package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ReserveRoomFragment extends BaseFragment implements View.OnClickListener, DateDialog.OnDateDialogListener, AmountPeopleDialog.OnAmountPeopleDialogListener {
    private static final String TAG = ReserveRoomFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "lodging";

    private Lodging mLodging;
    private OnReserveRoomFragmentListener mListener;

    private Spinner spinnerRooms;
    private SpinnerRoomsAdapter mSpinnerRoomsAdapter;
    private InputTextView mDates, mEnrollment, mName, mLastName, mEmail, mPhone, mAmountPeople, mAdditionalInformation;

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
        List<Room> list = new ArrayList<>();
        list.add(getDefaultRoom());
        mSpinnerRoomsAdapter = new SpinnerRoomsAdapter(getContext(), list);
        spinnerRooms.setAdapter(mSpinnerRoomsAdapter);

        mDates = (InputTextView)view.findViewById(R.id.itv_dates);
        mDates.setOnClickListener(this);
        mEnrollment = (InputTextView)view.findViewById(R.id.itv_enrollment);
        mName = (InputTextView)view.findViewById(R.id.itv_name);
        mLastName = (InputTextView)view.findViewById(R.id.itv_last_name);
        mEmail = (InputTextView)view.findViewById(R.id.itv_email);
        mPhone = (InputTextView)view.findViewById(R.id.itv_phone);
        mAmountPeople = (InputTextView)view.findViewById(R.id.itv_amount_people);
        mAmountPeople.setOnClickListener(this);
        mAdditionalInformation = (InputTextView)view.findViewById(R.id.itv_additional_information);

        Button mConfirm = (Button)view.findViewById(R.id.b_confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showToastMessage("Confirm test");
            }
        });
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
        String urlLodging = String.format("%s%s%s%s", API.LODGINGS, "/", mLodging.getId(), API.ROOMS);
        Call<List<Room>> listCall = apiClient.getRoomList(urlLodging, Session.getInstance().getUser().getApiToken());
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

    private void reserveRoom(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReserveRoomFragmentListener) {
            mListener = (OnReserveRoomFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnReserveRoomFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        }
    }

    @Override
    public void onDateSelected(String textDate, Date date) {
        if(mDates.getText().isEmpty()){
            mDates.setText(textDate);
        }else {
            mDates.setText(String.format("%s%s%s", mDates.getText(), ",", textDate));
        }

    }

    @Override
    public void onNumberPeopleSelected(int numberPeople) {
        mAmountPeople.setText(String.valueOf(numberPeople));
    }

    public interface OnReserveRoomFragmentListener {
        void onReserveRoomSuccessful();
    }
}
