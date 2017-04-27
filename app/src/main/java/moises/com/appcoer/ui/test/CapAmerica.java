package moises.com.appcoer.ui.test;

import javax.inject.Inject;

public class CapAmerica {
    Shield mShield;

    @Inject
    public CapAmerica(Shield shield){
        mShield = shield;
    }

    public String getWeaponName(){
        return mShield.getName();
    }
}
