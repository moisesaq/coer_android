package moises.com.appcoer.ui.home.reserve;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.model.Room;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.adapters.SpinnerRoomsAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.dialogs.AmountPeopleDialog;
import moises.com.appcoer.ui.dialogs.DateCustomDialog;
import moises.com.appcoer.ui.customviews.InputTextView;

//TODO Improve all
public class ReserveRoomFragment extends BaseFragment implements
        AdapterView.OnItemSelectedListener, AmountPeopleDialog.OnAmountPeopleDialogListener,
        InputTextView.Callback, DateCustomDialog.OnDateCustomDialogListener,
        TextView.OnEditorActionListener, ReserveRoomContract.View{
    public static final int ID_PARANA = 1;
    public static final int ID_TIMBUES = 2;
    private static final int ID_ROOM_DEFAULT = 1010;
    private static final String ARG_PARAM1 = "hotelId";

    @BindView(R.id.tv_hotel_title) protected TextView tvHotelTitle;
    @BindView(R.id.spinner_rooms) protected Spinner spinnerRooms;
    @BindView(R.id.progressBar) protected ProgressBar progressBar;
    @BindView(R.id.itv_from_date) protected InputTextView itvFromDate;
    @BindView(R.id.itv_to_date) protected InputTextView itvToDate;
    @BindView(R.id.itv_name) protected InputTextView itvName;
    @BindView(R.id.itv_last_name) protected InputTextView itvLastName;
    @BindView(R.id.itv_email) protected InputTextView itvEmail;
    @BindView(R.id.itv_phone) protected InputTextView itvPhone;
    @BindView(R.id.itv_amount_people) protected InputTextView itvAmountPeople;
    @BindView(R.id.itv_additional_information) protected InputTextView itvAdditionalInformation;
    @BindView(R.id.btn_confirm) protected Button btnConfirm;

    private int hotelId;
    private SpinnerRoomsAdapter mSpinnerRoomsAdapter;
    private Room mRoom;
    private List<String> dateListForReserve;
    private String[] textDatesNotAvailable;
    private Date fromDate, toDate;
    private Unbinder unbinder;
    private ReserveRoomContract.Presenter reserveRoomPresenter;

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
        new ReserveRoomPresenter(this);
        if (getArguments() != null)
            hotelId = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve_room, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUp();
        return view;
    }

    private void setUp(){
        spinnerRooms.setOnItemSelectedListener(this);
        List<Room> list = new ArrayList<>();
        list.add(getDefaultRoom());
        mSpinnerRoomsAdapter = new SpinnerRoomsAdapter(getContext(), list);
        spinnerRooms.setAdapter(mSpinnerRoomsAdapter);

        itvFromDate.setEnabled(false);
        itvFromDate.addCallback(this);

        itvToDate.setEnabled(false);
        itvToDate.addCallback(this);

        itvName.getEditText().setOnEditorActionListener(this);
        itvLastName.getEditText().setOnEditorActionListener(this);
        itvEmail.getEditText().setOnEditorActionListener(this);
        itvPhone.getEditText().setOnEditorActionListener(this);

        itvAmountPeople.setEnabled(false);
        itvAmountPeople.addCallback(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reserveRoomPresenter.onFragmentStarted();
        reserveRoomPresenter.loadRooms(hotelId);
    }

    @OnClick(R.id.btn_confirm)
    public void onClick(View view) {
        if(mRoom != null || mRoom.getId() != 1010)
            reserveRoomPresenter.reserveRoom();
    }

    private Room getDefaultRoom(){
        Room room = new Room();
        room.setId(ID_ROOM_DEFAULT);
        room.setRoomText(getString(R.string.select_room));
        return room;
    }

    @Override
    public void onActionIconClick(View view) {
        Log.d("RESERVE", " >>> " + view.getId());
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
    public void onDateSelected(String textDate, Date date, DateCustomDialog.ReserveDate reserveDate) {
        if(reserveDate == DateCustomDialog.ReserveDate.FROM_DATE){
            itvFromDate.setText(textDate);
            fromDate = date;
        }else {
            itvToDate.setText(textDate);
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
        itvAmountPeople.setText(String.valueOf(numberPeople));
        itvAdditionalInformation.requestFocus();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        clearDates();
        textDatesNotAvailable = null;
        mRoom = (Room) adapterView.getAdapter().getItem(i);
        if(mRoom.getId() != ID_ROOM_DEFAULT)
            reserveRoomPresenter.loadDatesBusyOfRoom(mRoom);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void clearDates(){
        itvFromDate.clearField();
        fromDate = null;
        itvToDate.clearField();
        toDate = null;
    }

    public static List<String> getDatesString(List<Date> dateList){
        List<String> stringList = new ArrayList<>();
        for (Date date: dateList)
            stringList.add(Utils.getCustomizedDate(Utils.DATE_FORMAT_INPUT, date));
        return stringList;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT){
            if(itvName.hasFocus()){
                itvLastName.requestFocus();
            }else if(itvEmail.hasFocus()){
                itvPhone.requestFocus();
            }else if(itvPhone.hasFocus()){
                AmountPeopleDialog.newInstance(1, 4, this).show(getFragmentManager(), AmountPeopleDialog.TAG);
            }
        }
        return false;
    }

    /**
     * IMPLEMENTATION RESERVE ROOM CONTRACT VIEW
     **/
    @Override
    public void setPresenter(ReserveRoomContract.Presenter presenter) {
        if(presenter != null) this.reserveRoomPresenter = presenter;
        else throw new RuntimeException("Reserve room presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void setTitle() {
        setTitle(getString(R.string.title_reserve_room));
        if(hotelId == ID_PARANA)
            tvHotelTitle.setText(getString(R.string.lodging_panana));
    }

    @Override
    public void showLoading(boolean show) {
        if(show) GlobalManager.showProgressDialog();
        else GlobalManager.dismissProgressDialog();
    }

    @Override
    public void showProgress(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void showRoomsAvailable(List<Room> rooms) {
        mSpinnerRoomsAdapter.addAll(rooms);
    }

    @Override
    public Reservation prepareReservation() {
        return createReservation();
    }

    @Override
    public boolean isAllDataValid() {
        return itvFromDate.isTextValid() && itvToDate.isTextValid() &&
                itvName.isTextValid() && itvLastName.isTextValid() && itvEmail.isEmailValid() &&
                itvPhone.isPhoneValid() && itvAmountPeople.isTextValid() &&
                itvAdditionalInformation.isTextValid();
    }

    @Override
    public void showReservationSuccess() {
        Utils.showDialogMessage("", getString(R.string.message_reservation_successful), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });
    }

    @Override
    public void showMessageError(int stringId) {
        Utils.showDialogMessage("", getSafeString(stringId), null);
    }

    @Override
    public void setUpTextDatesNoAvailable(List<String> listDateNoAvailable) {
        textDatesNotAvailable = new String[listDateNoAvailable.size()];
        textDatesNotAvailable = listDateNoAvailable.toArray(textDatesNotAvailable);
        showDateDialog(DateCustomDialog.ReserveDate.FROM_DATE);
    }

    private Reservation createReservation(){
        Reservation reservation = new Reservation();
        reservation.setRoomId(mRoom.getId());
        reservation.setDates(dateListForReserve);
        reservation.setName(itvName.getText());
        reservation.setLastName(itvLastName.getText());
        reservation.setEmail(itvEmail.getText());
        reservation.setPhone(itvPhone.getText());
        reservation.setAmountPeople(Integer.parseInt(itvAmountPeople.getText()));
        reservation.setDetail(itvAdditionalInformation.getText());
        return reservation;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
    }
}
