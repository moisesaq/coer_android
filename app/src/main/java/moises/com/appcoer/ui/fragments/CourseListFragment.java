package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.CourseList;
import moises.com.appcoer.tools.EndlessRecyclerOnScrollListener;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.adapters.CourseListAdapter;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseListFragment extends BaseFragment implements CourseListAdapter.CallBack{

    private static final String TAG = CourseListFragment.class.getSimpleName();
    private OnCoursesFragmentListener mListener;

    private View view;
    private RecyclerView mRecyclerView;
    private LoadingView mLoadingView;
    private CourseListAdapter mCourseListAdapter;
    private ProgressBar mProgressBar;

    public CourseListFragment() {
        // Required empty public constructor
    }

    public static CourseListFragment newInstance() {
        return new CourseListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            setupView();
        }
        setTitle(getString(R.string.nav_courses), R.id.nav_courses);
        return view;
    }

    private void setupView(){
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mCourseListAdapter = new CourseListAdapter(getContext(), new ArrayList<Course>(), this);
        mRecyclerView.setAdapter(mCourseListAdapter);
        mLoadingView.showLoading(mRecyclerView);
        getCourses(1);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getCourses(currentPage);
            }
        });
    }

    protected void getCourses(final int page) {
        Log.d(TAG, " PAGE >>> " + page);
        if(page > 1)
            mProgressBar.setVisibility(View.VISIBLE);
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<CourseList> courseListCall = apiClient.getCourses(null, page);
        courseListCall.enqueue(new Callback<CourseList>() {
            @Override
            public void onResponse(Call<CourseList> call, Response<CourseList> response) {
                if(response.isSuccessful() && response.body() != null && response.body().getCourses() != null && response.body().getCourses().size() > 0){
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                    mLoadingView.hideLoading("", mRecyclerView);
                    mCourseListAdapter.addItems(response.body().getCourses());
                }else if (page == 1){
                    mLoadingView.hideLoading(getSafeString(R.string.message_without_courses), mRecyclerView);
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CourseList> call, Throwable t) {
                mLoadingView.hideLoading(getSafeString(R.string.message_something_went_wrong), mRecyclerView);
            }
        });
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
    public void onCourseClick(Course course) {
        replaceFragment(CourseFragment.newInstance(course), true);
    }

    public interface OnCoursesFragmentListener {
        void onCourseClick(Course course);
    }
}
