package moises.com.appcoer.ui.login;

import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public class LoginContract {

    interface View extends BaseView<Presenter> {
        void showProgress(boolean show);

        void setEmailError(String error);

        void setPasswordError(String error);

        void showLoginError(String msg);

        void showGooglePlayServicesDialog(int errorCode);

        void showGooglePlayServicesError();

        void showNetworkError();
    }

    interface Presenter extends BasePresenter {
        void attemptLogin(String email, String password);
    }
}
