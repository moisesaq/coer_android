package moises.com.appcoer.ui.home.course.list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.tools.EndlessRecyclerOnScrollListener;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.adapters.CourseListAdapter;
import moises.com.appcoer.ui.home.course.detail.CourseFragment;
import moises.com.appcoer.ui.view.LoadingView;

public class CourseListFragment extends BaseFragment implements CourseListAdapter.CallBack, CourseListContract.View{

    private View view;
    @BindView(R.id.recycler_view) protected RecyclerView recyclerView;
    @BindView(R.id.loading_view) protected LoadingView loadingView;
    @BindView(R.id.progressBar) protected ProgressBar progressBar;

    private CourseListAdapter mCourseListAdapter;
    private OnCoursesFragmentListener mListener;
    private CourseListContract.Presenter courseListPresenter;
    private Unbinder unbinder;

    public static CourseListFragment newInstance() {
        return new CourseListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CourseListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            unbinder = ButterKnife.bind(this, view);
            setUp();
        }
        setTitle(getString(R.string.nav_courses), R.id.nav_courses);
        return view;
    }

    private void setUp(){
        //TODO Change to DI Dagger2
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        mCourseListAdapter = new CourseListAdapter(getContext(), new ArrayList<Course>(), this);
        recyclerView.setAdapter(mCourseListAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                courseListPresenter.loadCourses(currentPage);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseListPresenter.onFragmentStarted();
    }

    @Override
    public void onCourseClick(Course course) {
        replaceFragment(CourseFragment.newInstance(course), true);
    }

    /**
     * IMPLEMENTATION COURSE LIST CONTRACT
     * */
    @Override
    public void setPresenter(CourseListContract.Presenter presenter) {
        if(presenter != null) this.courseListPresenter = presenter;
        else throw new RuntimeException("Course list presenter can not be null");

    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showLoading(boolean show) {
        if(show) loadingView.showLoading(recyclerView);
        else loadingView.hideLoading("", recyclerView);
    }

    @Override
    public void showProgress(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void showMessageError(int stringId) {
        loadingView.hideLoading(getSafeString(stringId), recyclerView);
    }

    @Override
    public void showCourses(List<Course> courses) {
        mCourseListAdapter.addItems(courses);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCoursesFragmentListener) {
            mListener = (OnCoursesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public interface OnCoursesFragmentListener {
        void onCourseClick(Course course);
    }
}
