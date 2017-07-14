package moises.com.appcoer.injection.splash;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import moises.com.appcoer.global.session.SessionHandler;
import moises.com.appcoer.injection.util.ScopeActivity;
import moises.com.appcoer.ui.splash.SplashActivity;
import moises.com.appcoer.ui.splash.SplashContract;
import moises.com.appcoer.ui.splash.SplashPresenter;

@Module
public abstract class SplashActivityModule {

    @Binds
    abstract SplashContract.View providerSplashActivity(SplashActivity splashActivity);

    @Binds
    @IntoMap
    @ActivityKey(SplashActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindActivitySplashInjectorFactory(
            SplashActivityComponent.Builder builder);

    @Provides
    @ScopeActivity
    static SplashContract.Presenter provideSplashPresenter(SplashContract.View splashView, SessionHandler session){
        return new SplashPresenter(splashView, session);
    }
}
