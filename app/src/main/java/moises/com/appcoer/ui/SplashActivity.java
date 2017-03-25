package moises.com.appcoer.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import moises.com.appcoer.R;

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
        new CountDownTimer(3000, 1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                showLoginActivity();
            }
        }.start();
    }

    private void recoverSession(){

    }

    private void showLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}
