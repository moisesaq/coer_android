package moises.com.appcoer.injection.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import moises.com.appcoer.api.ApiService;
import moises.com.appcoer.api.DataContract;
import moises.com.appcoer.api.DataManager;
import moises.com.appcoer.global.session.SessionContract;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.injection.login.LoginActivityComponent;
import moises.com.appcoer.injection.main.MainActivityComponent;
import moises.com.appcoer.injection.splash.SplashActivityComponent;
import moises.com.appcoer.ui.CoerApplication;

@Module(subcomponents = {SplashActivityComponent.class, LoginActivityComponent.class, MainActivityComponent.class})
public class AppModule {

    @Singleton
    @Provides
    Context provideContext(CoerApplication coerApplication) {
        return coerApplication.getApplicationContext();
    }

    @Singleton
    @Provides
    SharedPreferences providerSharedPreferences(CoerApplication coerApplication) {
        return PreferenceManager.getDefaultSharedPreferences(coerApplication);
    }

    @Provides
    @Singleton
    static DataContract providerDataManager(ApiService apiService) {
        return new DataManager(apiService);
    }

    @Provides
    @Singleton
    SessionContract providerSessionManager(Context context) {
        return new SessionManager(context);
    }
}
