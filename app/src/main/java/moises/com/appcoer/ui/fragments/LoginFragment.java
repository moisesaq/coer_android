package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.reactivestreams.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.model.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseLoginFragment;
import moises.com.appcoer.ui.view.InputTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends BaseLoginFragment{
    public static final String TAG = LoginFragment.class.getSimpleName();

    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";

    @BindView(R.id.itv_user_name) protected InputTextView mUserName;
    @BindView(R.id.itv_password) protected InputTextView mPassword;
    @BindView(R.id.b_login) protected Button mLogin;

    private OnLoginFragmentListener mListener;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setupView(view, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    private void setupView(View view, Bundle savedInstanceState){
        setTitle(getString(R.string.app_full_name1), getString(R.string.app_full_name2));
        mUserName = (InputTextView)view.findViewById(R.id.itv_user_name);
        mPassword = (InputTextView)view.findViewById(R.id.itv_password);
        if(savedInstanceState != null){
            mUserName.setText(savedInstanceState.getString(USER_NAME, ""));
            mPassword.setText(savedInstanceState.getString(PASSWORD, ""));
        }
    }

    @OnClick({R.id.b_login, R.id.b_guest, R.id.tv_forgot_password})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_login:
                if(mUserName.isTextValid() && mPassword.isPasswordValid())
                    startLogin(mUserName.getText(), mPassword.getText());
                break;
            case R.id.b_guest:
                mListener.onStartGuest();
                break;
            case R.id.tv_forgot_password:
                mListener.onForgotPasswordClick();
                break;
        }
    }

    private void startLogin(String userName, String password){
        GlobalManager.showProgressDialog();
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        apiClient.startLogin(userName, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> mListener.onLoginSuccessful(user), error -> {
                    Utils.showDialogMessage("", getString(R.string.message_something_went_wrong), null);
                    Log.d(TAG, "ERROR >>> " + error.toString());
                });
    }

    private void login(String userName, String password){
        GlobalManager.showProgressDialog();
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<User> userCall = apiClient.login(userName, password);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                GlobalManager.dismissProgressDialog();
                if(response.isSuccessful() && response.body() != null){
                    mListener.onLoginSuccessful(response.body());
                }else{
                    Utils.showDialogMessage("", getString(R.string.message_something_went_wrong), null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "ERROR >>> " + t.toString());
                Utils.showDialogMessage("", getString(R.string.message_something_went_wrong), null);
                GlobalManager.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentListener) {
            mListener = (OnLoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLoginFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(USER_NAME, mUserName.getText());
        savedInstanceState.putString(PASSWORD, mPassword.getText());
        super.onSaveInstanceState(savedInstanceState);
    }

    public interface OnLoginFragmentListener {
        void onLoginSuccessful(User user);
        void onStartGuest();
        void onForgotPasswordClick();
    }
}
