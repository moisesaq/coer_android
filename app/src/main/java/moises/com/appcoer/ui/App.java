package moises.com.appcoer.ui;

import android.app.Application;
import android.content.Context;

import moises.com.appcoer.ui.test.DaggerWeaponComponent;
import moises.com.appcoer.ui.test.dagger.WeaponComponent;

public class App extends Application{

    private static Context mContext;
    private WeaponComponent weaponComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        weaponComponent = DaggerWeaponComponent.create();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public WeaponComponent getWeaponComponent(){
        return weaponComponent;
    }
}
