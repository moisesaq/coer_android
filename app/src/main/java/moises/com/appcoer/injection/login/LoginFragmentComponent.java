package moises.com.appcoer.injection.login;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import moises.com.appcoer.injection.util.ScopeFragment;
import moises.com.appcoer.ui.login.LoginFragment;

@ScopeFragment
@Subcomponent(modules = LoginFragmentModule.class)
public interface LoginFragmentComponent extends AndroidInjector<LoginFragment>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LoginFragment>{}
}
