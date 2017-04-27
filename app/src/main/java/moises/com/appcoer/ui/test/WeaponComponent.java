package moises.com.appcoer.ui.test;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules =  WeaponModule.class)
public interface WeaponComponent {
    void inject(TestFragment testFragment);
}
