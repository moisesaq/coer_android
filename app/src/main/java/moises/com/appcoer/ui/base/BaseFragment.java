package moises.com.appcoer.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import moises.com.appcoer.R;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.ui.MainActivity;

public class BaseFragment extends Fragment{

    private MainActivity mainActivity;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
        setHasOptionsMenu(true);

        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);

        /*if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }*/
    }

    protected void setTitle(@NonNull String title){
        if(toolbar != null)
            toolbar.setTitle(title);
    }

    protected void replaceFragment(Fragment fragment, boolean stack){
        if(mainActivity != null)
            mainActivity.showFragment(fragment, stack);
    }

    protected void onBackPressed(){
        if(mainActivity != null)
            mainActivity.onBackPressed();
    }

}
