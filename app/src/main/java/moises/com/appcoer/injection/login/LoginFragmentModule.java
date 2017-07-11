package moises.com.appcoer.injection.login;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import moises.com.appcoer.injection.util.ScopeFragment;
import moises.com.appcoer.ui.login.LoginContract;
import moises.com.appcoer.ui.login.LoginFragment;
import moises.com.appcoer.ui.login.LoginPresenter;

@Module
public abstract class LoginFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindLoginFragmentInjectorFactory(
            LoginFragmentComponent.Builder builder);

    @Provides
    @ScopeFragment
    static LoginContract.Presenter provideLoginFragmentPresenter(LoginContract.View loginFragmentView){
        return new LoginPresenter(loginFragmentView);
    }
}
