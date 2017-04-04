package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import moises.com.appcoer.R;
import moises.com.appcoer.ui.App;
import moises.com.appcoer.ui.base.IntroduceItem;
import moises.com.appcoer.ui.base.IntroducePageAdapter;

public class OnBoardingFragment extends BaseLoginFragment implements View.OnClickListener{
    private View view;
    private OnBoardingFragmentListener listener;

    public static OnBoardingFragment newInstance() {
        return new OnBoardingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_on_boarding, container, false);
        hideToolbar();
        setup();
        return view;
    }

    private void setup(){
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_on_boarding);
        IntroducePageAdapter adapter = new IntroducePageAdapter(getItemsOnBoarding());
        adapter.setColorText(android.R.color.white);
        viewPager.setAdapter(adapter);

        CircleIndicator circleIndicator = (CircleIndicator)view.findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
        Button bStarted = (Button) view.findViewById(R.id.b_started);
        bStarted.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_started:
                goToLogin();
                break;
        }
    }

    private void goToLogin(){
        onBoardingCompleted();
        listener.onGetStartClick();
    }

    public interface OnBoardingFragmentListener {
        void onGetStartClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnBoardingFragmentListener) {
            listener = (OnBoardingFragmentListener) context;
        }else{
            throw new ClassCastException(context.toString() + " must implement OnBoardingFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showToolbar();
    }

    private List<IntroduceItem> getItemsOnBoarding(){
        List<IntroduceItem> list = new ArrayList<>();
        list.add(new IntroduceItem(R.mipmap.onboarding_news, R.string.on_boarding_title1, R.string.on_boarding_description1));
        list.add(new IntroduceItem(R.mipmap.onboarding_course, R.string.on_boarding_title2, R.string.on_boarding_description2));
        list.add(new IntroduceItem(R.mipmap.onboarding_account, R.string.on_boarding_title3, R.string.on_boarding_description3));
        list.add(new IntroduceItem(R.mipmap.onboarding_reservation, R.string.on_boarding_title4, R.string.on_boarding_description4));

        return list;
    }

    public static Boolean isOnBoardingCompleted(){
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(OnBoardingFragment.class.getSimpleName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("on_boarding", false);
    }

    private void onBoardingCompleted(){
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(OnBoardingFragment.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putBoolean("on_boarding", true);
        editor.apply();
    }
}
