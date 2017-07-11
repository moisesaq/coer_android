package moises.com.appcoer.injection.login;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import moises.com.appcoer.injection.util.ScopeActivity;
import moises.com.appcoer.ui.login.LoginActivity;

@ScopeActivity
@Subcomponent(modules = { LoginActivityModule.class, LoginFragmentModule.class})
public interface LoginActivityComponent extends AndroidInjector<LoginActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LoginActivity>{}
}
