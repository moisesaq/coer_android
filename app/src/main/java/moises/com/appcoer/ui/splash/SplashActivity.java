package moises.com.appcoer.ui.splash;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import moises.com.appcoer.R;
import moises.com.appcoer.ui.main.MainActivity;
import moises.com.appcoer.ui.login.LoginActivity;
import moises.com.appcoer.ui.onBoarding.OnBoardingActivity;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    @Inject SplashContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setUpFullScreen();
        setContentView(R.layout.activity_splash);
        presenter.onActivityCreated();
    }

    private void setUpFullScreen(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            android.view.View decorView = getWindow().getDecorView();
            int uiOptions = android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void startOnBoardingActivity() {
        OnBoardingActivity.startActivity(this);
        finish();
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.startActivity(this);
        finish();
    }

    @Override
    public void startMainActivity() {
        MainActivity.startActivity(this);
        finish();
    }
}
