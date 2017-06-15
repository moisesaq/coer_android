package moises.com.appcoer.ui.onBoarding;

public interface OnBoardingContract {

    interface ActivityView{
        void initialize();

        void startLoginActivity();
    }

    interface ActivityPresenter{
        void onActivityCreated();

        void completeOnBoarding();

        void onActivityDestroyed();
    }
}
