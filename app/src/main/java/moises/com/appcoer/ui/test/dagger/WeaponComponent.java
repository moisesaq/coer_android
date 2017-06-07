package moises.com.appcoer.ui.test.dagger;

import javax.inject.Singleton;

import dagger.Component;
import moises.com.appcoer.ui.test.TestFragment;

@Singleton
@Component(modules =  WeaponModule.class)
public interface WeaponComponent {
    void inject(TestFragment testFragment);
}
