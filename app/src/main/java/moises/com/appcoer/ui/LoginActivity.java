package moises.com.appcoer.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import moises.com.appcoer.R;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.LogEvent;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.User;
import moises.com.appcoer.ui.dialogs.ResetPasswordDialog;
import moises.com.appcoer.ui.fragments.ChangePasswordFragment;
import moises.com.appcoer.ui.fragments.LoginFragment;
import moises.com.appcoer.ui.fragments.OnBoardingFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentListener,
                                                        OnBoardingFragment.OnBoardingFragmentListener, ChangePasswordFragment.OnChangePasswordFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GlobalManager.setActivityGlobal(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showFragment(OnBoardingFragment.newInstance(), true);
        setup();
    }

    private void setup(){
        if(!OnBoardingFragment.isOnBoardingCompleted()){
            showFragment(OnBoardingFragment.newInstance(), true);
        }else{
            showFragment(LoginFragment.newInstance(), true);
        }
    }

    private void showFragment(Fragment fragment, boolean stack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(stack)
            ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.replace(R.id.activity_login, fragment);
        ft.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        GlobalManager.setActivityGlobal(this);
    }

    /*LOGIN FRAGMENT LISTENER*/
    @Override
    public void onLoginSuccessful(User user) {
        Session.getInstance().setUser(user);
        LogEvent.logEventFirebaseAnalytic(this, LogEvent.EVENT_START_SESSION);
        if(user.getFirstTime() == 1){
            showFragment(ChangePasswordFragment.newInstance(), true);
        }else{
            goToMainActivity(false);
        }
    }

    @Override
    public void onStartGuest() {
        goToMainActivity(false);
    }

    @Override
    public void onForgotPasswordClick() {
        ResetPasswordDialog.newInstance().show(getSupportFragmentManager(), ResetPasswordDialog.TAG);
    }

    private void goToMainActivity(boolean close){
        startActivity(new Intent(this, MainActivity.class));
        if(close)
            finish();
    }

    @Override
    public void onGetStartClick() {
        showFragment(LoginFragment.newInstance(), true);
    }

    @Override
    public void onChangePasswordSuccessful(User user) {
        Session.getInstance().setUser(user);
        goToMainActivity(true);
    }

}
