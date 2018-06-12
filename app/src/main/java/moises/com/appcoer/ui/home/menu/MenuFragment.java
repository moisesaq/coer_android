package moises.com.appcoer.ui.home.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;
import moises.com.appcoer.R;
import moises.com.appcoer.model.Enrollment;
import moises.com.appcoer.model.News;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.home.reserve.ReserveRoomFragment;

public class MenuFragment extends BaseFragment implements MenuContract.View {

    @Inject
    protected MenuContract.Presenter menuPresenter;

    protected @BindView(R.id.tv_date_enrollment)
    TextView tvDateEnrollment;
    protected @BindView(R.id.tv_description_enrollment)
    TextView tvDescriptionEnrollment;
    protected @BindView(R.id.cv_news)
    CardView cvNews;
    protected @BindView(R.id.iv_image_news)
    ImageView ivImageNews;
    protected @BindView(R.id.tv_title_news)
    TextView tvNewsTitle;
    protected @BindView(R.id.tv_description_news)
    TextView tvNewsDescription;

    private OnMenuFragmentListener listener;
    private View view;
    private News news;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof OnMenuFragmentListener) {
            listener = (OnMenuFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnMenuFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_menu, container, false);
            ButterKnife.bind(this, view);
        }
        setTitle(getString(R.string.app_name));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuPresenter.onFragmentStarted();
    }

    @OnClick({R.id.cv_news, R.id.ly_news, R.id.ly_courses, R.id.ly_timbues, R.id.ly_parana,
            R.id.ly_process, R.id.ly_method_payment, R.id.ly_bills})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_news:
                if (news != null) listener.onImportantNewsClick(news);
                break;
            case R.id.ly_news:
                listener.onNewsClick();
                break;
            case R.id.ly_courses:
                listener.onCoursesClick();
                break;
            case R.id.ly_timbues:
                listener.onLodgingClick(ReserveRoomFragment.ID_TIMBUES);
                break;
            case R.id.ly_parana:
                listener.onLodgingClick(ReserveRoomFragment.ID_PARANA);
                break;
            case R.id.ly_process:
                listener.onProcessesClick();
                break;
            case R.id.ly_method_payment:
                listener.onMethodPaymentsClick();
                break;
            case R.id.ly_bills:
                listener.onBillsClick();
                break;
        }
    }

    /**
     * IMPLEMENTATION MENU CONTRACT VIEW
     */
    @Override
    public void setPresenter(MenuContract.Presenter presenter) {
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showEnrollment(@NonNull Enrollment enrollment) {
        tvDateEnrollment.setText(getCustomDate(enrollment.getDate()));
        tvDescriptionEnrollment.setText(enrollment.getDescription());
    }

    @Override
    public void showNewsOutstanding(@NonNull News news) {
        this.news = news;
        showNews();
    }

    private void showNews() {
        cvNews.setVisibility(View.VISIBLE);
        loadImage(news.getImage().getThumbnail(), ivImageNews);
        tvNewsTitle.setText(news.getTitle().trim());
        tvNewsDescription.setText(news.getContent());
    }

    private String getCustomDate(String textDate) {
        Date date = Utils.parseStringToDate(textDate, Utils.DATE_FORMAT_INPUT);
        return Utils.getCustomizedDate(Utils.DATE_FORMAT_DAY, date);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnMenuFragmentListener {
        void onImportantNewsClick(News news);

        void onNewsClick();

        void onCoursesClick();

        void onLodgingClick(int id);

        void onProcessesClick();

        void onMethodPaymentsClick();

        void onBillsClick();
    }
}
