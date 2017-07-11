package moises.com.appcoer.ui.login;

import android.support.v4.app.Fragment;

import moises.com.appcoer.model.User;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public class LoginContract {

    public interface View extends BaseView<Presenter> {
        void showLoading(boolean show);

        void showLoginError(String msg);

        void showNetworkError();

        String getUsername();

        String getPassword();

        boolean areUsernameAndPasswordValid();

        void loginSuccess(User user);
    }

    public interface Presenter extends BasePresenter {
        void startLogin();
    }
}
