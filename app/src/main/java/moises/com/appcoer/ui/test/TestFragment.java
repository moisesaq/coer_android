package moises.com.appcoer.ui.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moises.com.appcoer.R;
import moises.com.appcoer.ui.App;

public class TestFragment extends Fragment {

    @BindView(R.id.textView) protected TextView textView;
    @Inject CapAmerica mCapAmerica;

    public static TestFragment newInstance(){
        return new TestFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App)getActivity().getApplicationContext()).getWeaponComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button)
    public void onClick(View view){
        if(mCapAmerica != null)
            textView.setText(mCapAmerica.getWeaponName());
    }
}
