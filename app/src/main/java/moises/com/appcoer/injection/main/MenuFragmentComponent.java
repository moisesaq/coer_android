package moises.com.appcoer.injection.main;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import moises.com.appcoer.injection.util.ScopeFragment;
import moises.com.appcoer.ui.home.menu.MenuFragment;

@ScopeFragment
@Subcomponent(modules = MenuFragmentModule.class)
public interface MenuFragmentComponent extends AndroidInjector<MenuFragment>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MenuFragment>{}
}
