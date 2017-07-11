package moises.com.appcoer.injection.login;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import moises.com.appcoer.injection.util.ScopeActivity;
import moises.com.appcoer.ui.login.LoginActivity;
import moises.com.appcoer.ui.login.LoginContract;
import moises.com.appcoer.ui.login.LoginFragment;

@Module(subcomponents = LoginFragmentComponent.class)
public abstract class LoginActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(LoginActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindLoginActivityInjectorFactory(
            LoginActivityComponent.Builder builder);

    @Provides
    @ScopeActivity
    static LoginContract.View provideLoginFragment(){
        return new LoginFragment();
    }
}
