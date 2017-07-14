package moises.com.appcoer.ui.splash;

public interface SplashContract {

    interface View {
        void startOnBoardingActivity();

        void startLoginActivity();

        void startMainActivity();
    }

    interface Presenter {
        void onActivityCreated();

        void onActivityDestroyed();
    }
}
