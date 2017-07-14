package moises.com.appcoer.injection.splash;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import moises.com.appcoer.injection.util.ScopeActivity;
import moises.com.appcoer.ui.splash.SplashActivity;

@ScopeActivity
@Subcomponent(modules = SplashActivityModule.class)
public interface SplashActivityComponent extends AndroidInjector<SplashActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<SplashActivity>{}
}
