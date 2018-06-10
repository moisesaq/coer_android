package moises.com.appcoer.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import moises.com.appcoer.R;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.LogEvent;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.model.login.User;
import moises.com.appcoer.ui.main.MainActivity;
import moises.com.appcoer.ui.login.changePassword.ChangePasswordFragment;
import moises.com.appcoer.ui.login.forgotPassword.ForgotPasswordDialog;

public class LoginActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        OnLoginFragmentListener, ChangePasswordFragment.OnChangePasswordFragmentListener {

    @Inject
    DispatchingAndroidInjector<Fragment> injector;
    @Inject
    LoginContract.View loginFragmentView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GlobalManager.setActivityGlobal(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showFragment(loginFragmentView.getFragment(), false);
    }

    private void showFragment(Fragment fragment, boolean stack) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(stack ? fragment.getClass().getSimpleName() : null)
                .replace(R.id.activity_login, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalManager.setActivityGlobal(this);
    }

    /**
     * IMPLEMENTATION LOGIN FRAGMENT LISTENER
     */
    @Override
    public void onLoginSuccessful(User user) {
        SessionManager.getInstance(this).setUser(user);
        LogEvent.logEventFirebaseAnalytic(this, LogEvent.EVENT_START_SESSION);
        if (user.getFirstTime() == 1) {
            showFragment(ChangePasswordFragment.newInstance(), true);
        } else {
            goToMainActivity(true);
        }
    }

    @Override
    public void onStartAsGuest() {
        goToMainActivity(false);
    }

    @Override
    public void onForgotPasswordClick() {
        ForgotPasswordDialog.newInstance().show(getSupportFragmentManager(), ForgotPasswordDialog.TAG);
    }

    private void goToMainActivity(boolean close) {
        startActivity(new Intent(this, MainActivity.class));
        if (close) finish();
    }

    @Override
    public void onChangePasswordSuccessful(User user) {
        SessionManager.getInstance(this).setUser(user);
        goToMainActivity(true);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return injector;
    }
}
