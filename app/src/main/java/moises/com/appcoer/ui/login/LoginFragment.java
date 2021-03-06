package moises.com.appcoer.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;
import moises.com.appcoer.R;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.model.login.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseLoginFragment;
import moises.com.appcoer.ui.customviews.InputTextView;

public class LoginFragment extends BaseLoginFragment implements LoginContract.View {
    public static final String TAG = LoginFragment.class.getSimpleName();
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";

    @Inject
    LoginContract.Presenter presenter;

    @BindView(R.id.itv_user_name)
    protected InputTextView itvUserName;
    @BindView(R.id.itv_password)
    protected InputTextView itvPassword;
    @BindView(R.id.b_login)
    protected Button mLogin;

    private OnLoginFragmentListener listener;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
        if (context instanceof OnLoginFragmentListener) {
            listener = (OnLoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLoginFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setUp(savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    private void setUp(Bundle savedInstanceState) {
        setTitle(getString(R.string.app_full_name1), getString(R.string.app_full_name2));
        if (savedInstanceState != null) {
            itvUserName.setText(savedInstanceState.getString(USER_NAME, ""));
            itvPassword.setText(savedInstanceState.getString(PASSWORD, ""));
        }
    }

    @OnClick({R.id.b_login, R.id.b_guest, R.id.tv_forgot_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_login:
                presenter.startLogin();
                break;
            case R.id.b_guest:
                listener.onStartAsGuest();
                break;
            case R.id.tv_forgot_password:
                listener.onForgotPasswordClick();
                break;
        }
    }

    /**
     * IMPLEMENTATION LOGIN CONTRACT VIEW
     */
    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        if (presenter != null) this.presenter = presenter;
        else throw new RuntimeException("Login presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showLoading(boolean show) {
        if (show) GlobalManager.showProgressDialog();
        else GlobalManager.dismissProgressDialog();
    }

    @Override
    public void showLoginError(String message) {
        Utils.showDialogMessage("", message, null);
    }

    @Override
    public void showNetworkError() {
    }

    @Override
    public String getUsername() {
        return itvUserName.getText();
    }

    @Override
    public String getPassword() {
        return itvPassword.getText();
    }

    @Override
    public boolean areUsernameAndPasswordValid() {
        return itvUserName.isTextValid() && itvPassword.isPasswordValid();
    }

    @Override
    public void loginSuccess(User user) {
        listener.onLoginSuccessful(user);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString(USER_NAME, itvUserName.getText());
        savedInstanceState.putString(PASSWORD, itvPassword.getText());
        super.onSaveInstanceState(savedInstanceState);
    }

}
