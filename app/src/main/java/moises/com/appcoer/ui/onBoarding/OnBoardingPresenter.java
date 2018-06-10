package moises.com.appcoer.ui.onBoarding;

import android.content.Context;
import android.content.SharedPreferences;

import moises.com.appcoer.ui.CoerApplication;

public class OnBoardingPresenter implements OnBoardingContract.Presenter {

    private OnBoardingContract.View view;

    OnBoardingPresenter(OnBoardingContract.View view){
        this.view = view;
    }

    @Override
    public void completeOnBoarding() {
        onBoardingCompleted();
        view.startLoginActivity();
    }

    //TODO: Improve this
    public static Boolean isOnBoardingCompleted(){
        SharedPreferences sharedPreferences = CoerApplication.getContext()
                .getSharedPreferences(OnBoardingActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("on_boarding", false);
    }

    private void onBoardingCompleted(){
        SharedPreferences sharedPreferences = CoerApplication.getContext()
                .getSharedPreferences(OnBoardingActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putBoolean("on_boarding", true);
        editor.apply();
    }
}
