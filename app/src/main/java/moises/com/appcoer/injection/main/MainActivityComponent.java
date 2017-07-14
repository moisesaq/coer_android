package moises.com.appcoer.injection.main;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import moises.com.appcoer.injection.util.ScopeActivity;
import moises.com.appcoer.ui.main.MainActivity;

@ScopeActivity
@Subcomponent(modules = { MainActivityModule.class, MenuFragmentModule.class })
public interface MainActivityComponent extends AndroidInjector<MainActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{}
}
