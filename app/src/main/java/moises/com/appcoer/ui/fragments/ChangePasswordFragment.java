package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.view.InputTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends BaseLoginFragment implements View.OnClickListener{
    private static final String TAG = ChangePasswordFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private OnChangePasswordFragmentListener mListener;

    private InputTextView mEmail, mNewPassword, mRepeatNewPassword;

    public ChangePasswordFragment() {
    }

    public static ChangePasswordFragment newInstance(String param1) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        mEmail = (InputTextView)view.findViewById(R.id.itv_email);
        mNewPassword = (InputTextView)view.findViewById(R.id.itv_new_password);
        mRepeatNewPassword = (InputTextView)view.findViewById(R.id.itv_repeat_new_password);
        Button mConfirm = (Button)view.findViewById(R.id.b_confirm);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.b_confirm){
            if(mEmail.isEmailValid() && mNewPassword.isTextValid(getString(R.string.error_data_no_valid)) && mNewPassword.isTextValid(getString(R.string.error_data_no_valid))){
                changePassword();
            }
        }
    }

    private void changePassword(){
        if(!mNewPassword.getText().equals(mRepeatNewPassword.getText())){
            Utils.showToastMessage(getString(R.string.message_passwords_do_not_match));
            return;
        }

        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<User>> listCall = apiClient.changePassword(mNewPassword.getText(), mEmail.getText(), Session.getInstance().getUser().getApiToken());
        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                try {
                    Log.d(TAG, " SUCCESS >>> " + response.message() + " code >>> " + response.code());
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                    mListener.onChangePasswordSuccessful(response.body().get(0));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, " ERROR >>> " + t.toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChangePasswordFragmentListener) {
            mListener = (OnChangePasswordFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChangePasswordFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnChangePasswordFragmentListener {
        void onChangePasswordSuccessful(User user);
    }
}
