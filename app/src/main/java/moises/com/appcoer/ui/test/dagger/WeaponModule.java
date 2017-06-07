package moises.com.appcoer.ui.test.dagger;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WeaponModule {

    @Provides
    @Singleton
    public Shield provideShield(){
        return new Shield();
    }

    @Named("shield")
    @Provides
    @Singleton
    public CapAmerica provideHero(Shield shield){
        return new CapAmerica(shield);
    }

    @Named("test")
    @Provides
    public CapAmerica provideHero2(Shield shield){
        return new CapAmerica(shield);
    }
}
