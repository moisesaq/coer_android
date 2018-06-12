package moises.com.appcoer.ui.home.course.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseFragment;

public class CourseFragment extends BaseFragment implements CourseContract.View {
    private static final String ARG_PARAM1 = "course";

    @BindView(R.id.iv_course)
    protected ImageView imageView;
    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.tv_date)
    protected TextView tvDate;
    @BindView(R.id.tv_cost)
    protected TextView tvCost;
    @BindView(R.id.tv_discount)
    protected TextView tvDiscount;
    @BindView(R.id.tv_discount_to_date)
    protected TextView tvDiscountToDate;
    @BindView(R.id.wv_content)
    protected WebView webView;

    private Course course;
    private CourseContract.Presenter coursePresenter;
    private Unbinder unbinder;

    public static CourseFragment newInstance(Course course) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CoursePresenter(this);
        if (getArguments() != null)
            course = (Course) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coursePresenter.onFragmentStarted();
    }

    /**
     * IMPLEMENTATION COURSE CONTRACT VIEW
     */
    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        if (presenter != null) this.coursePresenter = presenter;
        else throw new RuntimeException("Course presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showCurrentCourse() {
        loadImage(course.getImage().getSlide(), imageView);
        showCourseDetail(course);
        showCourseContent(course.getContent());
        coursePresenter.updateCourse(course);
    }

    @Override
    public void showCourseUpdated(Course course) {
        showCourseContent(course.getContent());
    }

    private void showCourseDetail(Course course) {
        tvTitle.setText(course.getTitle().trim());
        tvDate.setText(Utils.getCustomDate(Utils.parseStringToDate(course.getDate(), Utils.DATE_FORMAT_INPUT)));
        tvCost.setText(String.format("%s %s", "$", course.getCost()));

        if (course.getDiscount() != null && !course.getDiscount().isEmpty()) {
            tvDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText(course.getDiscount());
        }

        if (course.getDiscountToDate() != null && !course.getDiscountToDate().isEmpty()) {
            tvDiscountToDate.setVisibility(View.VISIBLE);
            tvDiscountToDate.setText(course.getDiscountToDate());
        }
    }

    private void showCourseContent(String courseContent) {
        webView.loadData(courseContent, "text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
