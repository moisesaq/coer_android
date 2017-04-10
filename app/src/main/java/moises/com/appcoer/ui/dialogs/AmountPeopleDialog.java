package moises.com.appcoer.ui.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import moises.com.appcoer.R;

public class AmountPeopleDialog extends DialogFragment implements View.OnClickListener{

    public static final String TAG = "PEOPLE ON BOARD DIALOG";

    private NumberPicker npNumberPeople;
    private int numberPeople, numberMax;
    private OnAmountPeopleDialogListener onAmountPeopleDialogListener;

    public static AmountPeopleDialog newInstance(int numberPeople, int numberMax, OnAmountPeopleDialogListener listener){
        AmountPeopleDialog peopleOnBoardDialog = new AmountPeopleDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("numberPeople", numberPeople);
        bundle.putInt("numberMax", numberMax);
        peopleOnBoardDialog.setArguments(bundle);
        peopleOnBoardDialog.setOnAmountPeopleDialogListener(listener);
        return peopleOnBoardDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            numberPeople = getArguments().getInt("numberPeople", 1);
            numberMax = getArguments().getInt("numberMax", 4);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.amount_people);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_people_amount, null);
        npNumberPeople = (NumberPicker)view.findViewById(R.id.np_number_people);
        npNumberPeople.setMinValue(1);
        npNumberPeople.setMaxValue(numberMax);

        npNumberPeople.setValue(numberPeople > numberMax ? numberMax : numberPeople);

        Button cancel = (Button)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        Button save = (Button)view.findViewById(R.id.save);
        save.setOnClickListener(this);
        dialog.setView(view);

        return dialog.create();
    }

    public void setOnAmountPeopleDialogListener(OnAmountPeopleDialogListener listener){
        this.onAmountPeopleDialogListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.save:
                if(onAmountPeopleDialogListener != null){
                    onAmountPeopleDialogListener.onNumberPeopleSelected(npNumberPeople.getValue());
                    this.dismiss();
                }
                break;
        }
    }

    public interface OnAmountPeopleDialogListener {
        void onNumberPeopleSelected(int numberPeople);
    }
}
