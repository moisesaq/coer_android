package moises.com.appcoer.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import moises.com.appcoer.injection.app.AppComponent;
import moises.com.appcoer.injection.app.DaggerAppComponent;

public class CoerApplication extends Application implements HasActivityInjector{
    @Inject DispatchingAndroidInjector<Activity> injector;
    private static AppComponent appComponent;

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        setUpDagger();
    }

    public static Context getContext() {
        return mContext;
    }

    private void setUpDagger(){
        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build();
        appComponent.inject(this);
    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return injector;
    }
}
