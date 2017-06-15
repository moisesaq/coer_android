package moises.com.appcoer.ui.onBoarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import moises.com.appcoer.R;
import moises.com.appcoer.ui.base.IntroduceItem;
import moises.com.appcoer.ui.login.LoginActivity;

public class OnBoardingActivity extends AppCompatActivity implements OnBoardingContract.ActivityView{

    OnBoardingContract.ActivityPresenter onBoardingPresenter;

    @BindView(R.id.indicator) protected CircleIndicator mCircleIndicator;
    @BindView(R.id.vp_on_boarding) protected ViewPager mViewPager;
    @BindView(R.id.b_started) protected Button mStated;

    @BindColor(android.R.color.white) int white;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, OnBoardingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        ButterKnife.bind(this);
        setUp();
        onBoardingPresenter = new OnBoardingPresenter(this);
        onBoardingPresenter.onActivityCreated();
    }

    @OnClick(R.id.b_started)
    public void onClick(View v) {
        onBoardingPresenter.completeOnBoarding();
    }

    @Override
    public void initialize() {
        setUp();
    }

    private void setUp(){
        IntroducePageAdapter adapter = new IntroducePageAdapter(getItemsOnBoarding());
        adapter.setColorText(white);
        mViewPager.setAdapter(adapter);
        mCircleIndicator.setViewPager(mViewPager);
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.startActivity(this);
    }

    private List<IntroduceItem> getItemsOnBoarding(){
        List<IntroduceItem> list = new ArrayList<>();
        list.add(new IntroduceItem(R.mipmap.onboarding_news, R.string.on_boarding_title1, R.string.on_boarding_description1));
        list.add(new IntroduceItem(R.mipmap.onboarding_course, R.string.on_boarding_title2, R.string.on_boarding_description2));
        list.add(new IntroduceItem(R.mipmap.onboarding_account, R.string.on_boarding_title3, R.string.on_boarding_description3));
        list.add(new IntroduceItem(R.mipmap.onboarding_reservation, R.string.on_boarding_title4, R.string.on_boarding_description4));

        return list;
    }
}
