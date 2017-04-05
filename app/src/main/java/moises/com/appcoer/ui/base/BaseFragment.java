package moises.com.appcoer.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.ui.MainActivity;

public class BaseFragment extends Fragment{

    private MainActivity mainActivity;
    private ActionBar actionBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
        setHasOptionsMenu(true);

        actionBar = mainActivity.getSupportActionBar();

        /*if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }*/
    }

    protected void replaceFragment(Fragment fragment, boolean stack){
        if(mainActivity != null)
            mainActivity.showFragment(fragment, stack);
    }

}
