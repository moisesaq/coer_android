package moises.com.appcoer.ui;

/**
 * Created by moises on 3/28/17.
 */

public class Login {

    interface View extends BaseView<Presenter>{
        void showProgress(boolean show);

        void setEmailError(String error);

        void setPasswordError(String error);

        void showLoginError(String msg);

        void showGooglePlayServicesDialog(int errorCode);

        void showGooglePlayServicesError();

        void showNetworkError();
    }

    interface Presenter extends BasePresenter{
        void attemptLogin(String email, String password);
    }
}
