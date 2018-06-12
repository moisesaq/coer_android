package moises.com.appcoer.ui.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setUpActionBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void replaceFragment(Fragment fragment, int layoutId, boolean addToStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToStack)
            transaction.addToBackStack(fragment.getClass().getName());
        transaction.replace(layoutId, fragment);
        transaction.commit();
    }

    protected boolean popBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }
}