package moises.com.appcoer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import moises.com.appcoer.R;

public class BaseLoginFragment extends Fragment {
    private Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
    }

    protected void setTitle(@NonNull String title, String subtitle){
        if(toolbar != null){
            toolbar.setTitle(title);
            if(subtitle != null && !subtitle.isEmpty()){
                toolbar.setSubtitle(subtitle);
            }else{
                toolbar.setSubtitle("");
            }
        }
    }

    protected void hideToolbar(){
        if(toolbar != null)
            toolbar.setVisibility(View.GONE);
    }

    protected void showToolbar(){
        if (toolbar != null)
            toolbar.setVisibility(View.VISIBLE);
    }
}
