package moises.com.appcoer.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import moises.com.appcoer.R;
import moises.com.appcoer.global.Session;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void showMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
