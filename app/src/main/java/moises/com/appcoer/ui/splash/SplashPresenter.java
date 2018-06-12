package moises.com.appcoer.ui.splash;

import android.os.CountDownTimer;

import moises.com.appcoer.global.session.SessionContract;
import moises.com.appcoer.ui.onBoarding.OnBoardingPresenter;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View splashView;
    private SessionContract session;

    public SplashPresenter(SplashContract.View splashView, SessionContract session){
        this.splashView = splashView;
        this.session = session;
    }

    @Override
    public void onActivityCreated() {
        startSplash();
    }

    private void startSplash(){
        //TODO change to RXJava
        new CountDownTimer(2000, 1000){
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                recoverSession();
            }
        }.start();
    }

    private void recoverSession(){
        if(session.getUser() != null){
            splashView.startMainActivity();
            return;
        }

        if (!OnBoardingPresenter.isOnBoardingCompleted()){
            splashView.startOnBoardingActivity();
            return;
        }

        splashView.startLoginActivity();
    }

    @Override
    public void onActivityDestroyed() {
    }
}
