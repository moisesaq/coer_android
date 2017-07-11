package moises.com.appcoer.ui.onBoarding;

import android.content.Context;
import android.content.SharedPreferences;

import moises.com.appcoer.ui.CoerApplication;

public class OnBoardingPresenter implements OnBoardingContract.ActivityPresenter{

    private OnBoardingContract.ActivityView activityView;

    public OnBoardingPresenter(OnBoardingContract.ActivityView activityView){
        this.activityView = activityView;
    }

    @Override
    public void onActivityCreated() {
        activityView.initialize();
    }

    @Override
    public void completeOnBoarding() {
        onBoardingCompleted();
        activityView.startLoginActivity();
    }

    @Override
    public void onActivityDestroyed() {
    }

    public static Boolean isOnBoardingCompleted(){
        SharedPreferences sharedPreferences = CoerApplication.getContext().getSharedPreferences(OnBoardingActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("on_boarding", false);
    }

    private void onBoardingCompleted(){
        SharedPreferences sharedPreferences = CoerApplication.getContext().getSharedPreferences(OnBoardingActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putBoolean("on_boarding", true);
        editor.apply();
    }
}
