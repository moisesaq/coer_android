package moises.com.appcoer.ui.onBoarding;

public interface OnBoardingContract {

    interface View {

        void startLoginActivity();
    }

    interface Presenter {

        void completeOnBoarding();
    }
}
