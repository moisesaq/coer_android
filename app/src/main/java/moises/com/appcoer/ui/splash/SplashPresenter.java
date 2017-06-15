package moises.com.appcoer.ui.splash;

import android.os.CountDownTimer;

import moises.com.appcoer.global.Session;
import moises.com.appcoer.ui.onBoarding.OnBoardingPresenter;

public class SplashPresenter implements SplashContract.ActivityPresenter{

    private SplashContract.ActivityView activityView;

    public SplashPresenter(SplashContract.ActivityView activityView){
        this.activityView = activityView;
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
        //TODO Remote this set IFs
        if(Session.getInstance().getUser() != null){
            activityView.startMainActivity();
        }else if (OnBoardingPresenter.isOnBoardingCompleted()){
            activityView.startLoginActivity();
        }else {
            activityView.startOnBoardingActivity();
        }
    }

    @Override
    public void onActivityDestroyed() {
    }
}
