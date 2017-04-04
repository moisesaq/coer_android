package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import moises.com.appcoer.R;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.view.InputTextView;

public class ReserveRoomFragment extends BaseLoginFragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private OnFragmentInteractionListener mListener;

    private InputTextView mDNI, mEmail, mNewPassword, mRepeatNewPassword;

    public ReserveRoomFragment() {
    }

    public static ReserveRoomFragment newInstance(String param1) {
        ReserveRoomFragment fragment = new ReserveRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        setTitle(getString(R.string.title_change_password), "");
        mDNI = (InputTextView)view.findViewById(R.id.itv_dni);
        mEmail = (InputTextView)view.findViewById(R.id.itv_email);
        mNewPassword = (InputTextView)view.findViewById(R.id.itv_new_password);
        mRepeatNewPassword = (InputTextView)view.findViewById(R.id.itv_repeat_new_password);
        Button mConfirm = (Button)view.findViewById(R.id.b_confirm);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.b_confirm){
            Utils.showToastMessage("Confirm test");
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
