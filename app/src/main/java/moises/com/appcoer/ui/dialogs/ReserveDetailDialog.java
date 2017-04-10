package moises.com.appcoer.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import moises.com.appcoer.R;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.ui.view.TextImageView;

public class ReserveDetailDialog extends DialogFragment{

    public static final String TAG = ReserveDetailDialog.class.getSimpleName();
    private static final String ARG_PARAM_1 = "reservation";

    private Reservation reservation;

    public static ReserveDetailDialog newInstance(Reservation reservation){
        ReserveDetailDialog peopleOnBoardDialog = new ReserveDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM_1, reservation);
        peopleOnBoardDialog.setArguments(bundle);
        return peopleOnBoardDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            reservation = (Reservation) getArguments().getSerializable(ARG_PARAM_1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.reservation);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_reserve_detail, null);
        TextView mState = (TextView)view.findViewById(R.id.tv_state);
        mState.setText(reservation.getState());
        TextImageView mFullName = (TextImageView)view.findViewById(R.id.tiv_full_name);
        mFullName.setText1(String.format("%s %s", reservation.getName(), reservation.getLastName()));
        TextImageView mEmail = (TextImageView)view.findViewById(R.id.tiv_email);
        mEmail.setText1(reservation.getEmail());
        TextImageView mPhone = (TextImageView)view.findViewById(R.id.tiv_phone);
        mPhone.setText1(reservation.getPhone());
        TextImageView mLodging = (TextImageView)view.findViewById(R.id.tiv_lodging);
        mLodging.setText1(reservation.getLodging());
        TextImageView mRoom = (TextImageView)view.findViewById(R.id.tiv_room);
        mRoom.setText1(reservation.getRoomDescription());
        TextImageView mAmountPeople = (TextImageView)view.findViewById(R.id.tiv_amount_people);
        mAmountPeople.setText1(String.valueOf(reservation.getAmountPeople()));
        TextImageView mCreatedAt = (TextImageView)view.findViewById(R.id.tiv_created_at);
        mCreatedAt.setText1(reservation.getCreatedAt());
        TextImageView mDatesStay = (TextImageView)view.findViewById(R.id.tiv_dates_stay);
        mDatesStay.setText1(reservation.getDates().toString());
        TextImageView mAdditionalInformation = (TextImageView)view.findViewById(R.id.tiv_additional_information);
        mAdditionalInformation.setText1(reservation.getDetail());
        dialog.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        dialog.setView(view);
        return dialog.create();
    }
}
