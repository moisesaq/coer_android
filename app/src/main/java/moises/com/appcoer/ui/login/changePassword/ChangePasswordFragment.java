package moises.com.appcoer.ui.login.changePassword;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiService;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.model.login.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseLoginFragment;
import moises.com.appcoer.ui.customviews.InputTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends BaseLoginFragment implements View.OnClickListener{
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
        Button mConfirm = (Button)view.findViewById(R.id.btn_confirm);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_confirm){
            if(mEmail.isEmailValid() && mNewPassword.isPasswordValid() && mNewPassword.isPasswordValid()){
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
        ApiService apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<User>> listCall = apiClient.changePassword(
                mNewPassword.getText(), mEmail.getText(), SessionManager.getInstance(getActivity()).getUser().getApiToken());
        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                GlobalManager.dismissProgressDialog();
                if(response.isSuccessful()){
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
