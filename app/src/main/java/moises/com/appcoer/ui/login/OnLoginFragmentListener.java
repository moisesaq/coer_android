package moises.com.appcoer.ui.login;

import moises.com.appcoer.model.User;

public interface OnLoginFragmentListener {
    void onLoginSuccessful(User user);
    void onStartGuest();
    void onForgotPasswordClick();
}
