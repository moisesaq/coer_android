package moises.com.appcoer.ui;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import moises.com.appcoer.R;
import moises.com.appcoer.global.Session;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onResume(){
        super.onResume();
        startSplash();
    }

    private void startSplash(){
        new CountDownTimer(2000, 1000){
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                recoverSession();
            }
        }.start();
    }

    private void recoverSession(){
        if(Session.getInstance().getUser() != null){
            showMainActivity();
        }else {
            showLoginActivity();
        }
    }

    private void showLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
