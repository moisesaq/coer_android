package moises.com.appcoer.injection.main;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import moises.com.appcoer.api.DataHandler;
import moises.com.appcoer.global.session.SessionHandler;
import moises.com.appcoer.injection.util.ScopeActivity;
import moises.com.appcoer.ui.home.menu.MenuContract;
import moises.com.appcoer.ui.home.menu.MenuFragment;
import moises.com.appcoer.ui.main.MainActivity;
import moises.com.appcoer.ui.main.MainContract;
import moises.com.appcoer.ui.main.MainPresenter;

@Module(subcomponents = { MenuFragmentComponent.class })
public abstract class MainActivityModule {

    @Binds
    abstract MainContract.View provideMainActivity(MainActivity mainActivity);

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(
            MainActivityComponent.Builder builder);

    @Provides
    @ScopeActivity
    static MainContract.Presenter provideMainPresenter(MainContract.View mainView,
                                                       DataHandler dataManager,
                                                       SessionHandler session){
        return new MainPresenter(mainView, dataManager, session);
    }

    @Provides
    @ScopeActivity
    static MenuContract.View provideMenuFragment(){
        return new MenuFragment();
    }
}
