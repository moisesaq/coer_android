package moises.com.appcoer.ui.login;

import moises.com.appcoer.model.login.User;

public interface OnLoginFragmentListener {

    void onLoginSuccessful(User user);

    void onStartAsGuest();

    void onForgotPasswordClick();
}
