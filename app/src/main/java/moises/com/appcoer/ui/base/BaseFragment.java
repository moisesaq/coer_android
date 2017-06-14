package moises.com.appcoer.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import moises.com.appcoer.R;
import moises.com.appcoer.ui.home.MainActivity;

public class BaseFragment extends Fragment{

    private MainActivity mainActivity;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
        setHasOptionsMenu(true);

        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
    }

    protected void setTitle(@NonNull String title){
        setTitle(title, 0);
    }

    protected void setTitle(String title, int navId){
        if(toolbar != null)
            toolbar.setTitle(title);
        if(navigationView != null) {
            if(navId > 0)
                navigationView.setCheckedItem(navId);
            else
                uncheckedNavigation();
        }
    }

    private void uncheckedNavigation(){
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    protected void replaceFragment(Fragment fragment, boolean stack){
        if(mainActivity != null)
            mainActivity.showFragment(fragment, stack);
    }

    protected void onBackPressed(){
        if(mainActivity != null)
            mainActivity.onBackPressed();
    }

    protected String getSafeString(int resId){
        if(!isAdded())
            return "";
        return getString(resId);
    }
}
