package moises.com.appcoer.injection.main;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import moises.com.appcoer.api.DataContract;
import moises.com.appcoer.global.session.SessionContract;
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
                                                       DataContract dataManager,
                                                       SessionContract session){
        return new MainPresenter(mainView, dataManager, session);
    }

    @Provides
    @ScopeActivity
    static MenuContract.View provideMenuFragment(){
        return new MenuFragment();
    }
}
