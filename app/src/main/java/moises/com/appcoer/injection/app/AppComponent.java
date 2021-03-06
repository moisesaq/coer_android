package moises.com.appcoer.injection.app;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import moises.com.appcoer.injection.login.LoginActivityModule;
import moises.com.appcoer.injection.main.MainActivityModule;
import moises.com.appcoer.injection.splash.SplashActivityModule;
import moises.com.appcoer.ui.CoerApplication;
import moises.com.appcoer.ui.login.LoginActivity;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class, SplashActivityModule.class, LoginActivityModule.class,
        MainActivityModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(CoerApplication coerApplication);

        AppComponent build();
    }

    void inject(CoerApplication coerApplication);
}
