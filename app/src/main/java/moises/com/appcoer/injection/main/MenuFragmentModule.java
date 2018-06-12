package moises.com.appcoer.injection.main;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import moises.com.appcoer.api.DataContract;
import moises.com.appcoer.global.session.SessionContract;
import moises.com.appcoer.injection.util.ScopeFragment;
import moises.com.appcoer.ui.home.menu.MenuContract;
import moises.com.appcoer.ui.home.menu.MenuFragment;
import moises.com.appcoer.ui.home.menu.MenuPresenter;

@Module
public abstract class MenuFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(MenuFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindMenuFragmentInjectorFactory(
            MenuFragmentComponent.Builder builder);

    @Provides
    @ScopeFragment
    static MenuContract.Presenter provideMenuFragmentPresenter(MenuContract.View menuView,
                                                               DataContract dataManager,
                                                               SessionContract session){
        return new MenuPresenter(menuView, dataManager, session);
    }
}
