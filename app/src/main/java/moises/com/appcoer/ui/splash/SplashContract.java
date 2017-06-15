package moises.com.appcoer.ui.splash;

public interface SplashContract {

    interface ActivityView{
        void startOnBoardingActivity();

        void startLoginActivity();

        void startMainActivity();
    }

    interface ActivityPresenter{
        void onActivityCreated();

        void onActivityDestroyed();
    }
}
