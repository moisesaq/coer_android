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
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.view.InputTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends BaseLoginFragment implements View.OnClickListener{
    private static final String TAG = ChangePasswordFragment.class.getSimpleName();
    private OnChangePasswordFragmentListener mListener;

    private InputTextView mEmail, mNewPassword, mRepeatNewPassword;

    public ChangePasswordFragment() {
    }

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
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
        GlobalManager.showProgressDialog();
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<User>> listCall = apiClient.changePassword(mNewPassword.getText(), mEmail.getText(), Session.getInstance().getUser().getApiToken());
        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                GlobalManager.dismissProgressDialog();
                if(response.isSuccessful()){
                    Log.d(TAG, " SUCCESS >>> " + response.message() + " code >>> " + response.code());
                    mListener.onChangePasswordSuccessful(response.body().get(0));
                }else{
                    Utils.showToastMessage(getString(R.string.message_something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                GlobalManager.dismissProgressDialog();
                Utils.showToastMessage(getString(R.string.message_something_went_wrong));
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
