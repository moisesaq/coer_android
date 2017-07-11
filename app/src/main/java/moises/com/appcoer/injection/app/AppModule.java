package moises.com.appcoer.injection.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import moises.com.appcoer.injection.login.LoginActivityComponent;
import moises.com.appcoer.ui.CoerApplication;

@Module(subcomponents = { LoginActivityComponent.class })
public class AppModule {

    @Singleton
    @Provides
    Context provideContext(CoerApplication coerApplication){
        return coerApplication.getApplicationContext();
    }

    @Singleton
    @Provides
    SharedPreferences providerSharedPreferences(CoerApplication coerApplication){
        return PreferenceManager.getDefaultSharedPreferences(coerApplication);
    }
}
