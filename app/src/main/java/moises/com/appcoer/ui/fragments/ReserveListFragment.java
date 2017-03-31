package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.CourseList;
import moises.com.appcoer.model.News;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.fragments.adapters.NewsListAdapter;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveListFragment extends BaseFragment{

    private static final String TAG = ReserveListFragment.class.getSimpleName();

    private View view;
    private RecyclerView mRecyclerView;
    private LoadingView mLoadingView;
    private FloatingActionButton mAddReserve;
    //private CourseListAdapter mCourseListAdapter;

    public ReserveListFragment() {
        // Required empty public constructor
    }

    public static ReserveListFragment newInstance() {
        return new ReserveListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            setupView();
        }
        return view;
    }

    private void setupView(){
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        /*mCourseListAdapter = new CourseListAdapter(new ArrayList<Course>(), this);
        mRecyclerView.setAdapter(mCourseListAdapter);*/
        mAddReserve = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        mAddReserve.setVisibility(View.VISIBLE);
        showComingSoon();
    }

    private void showComingSoon(){
        mLoadingView.hideLoading("Reserves coming soon", mRecyclerView, R.mipmap.working);
    }

    private void getNews() {
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<CourseList> courseListCall = apiClient.getCourses(null, 1);
        courseListCall.enqueue(new Callback<CourseList>() {
            @Override
            public void onResponse(Call<CourseList> call, Response<CourseList> response) {
                Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                //mCourseListAdapter.addItems(response.body().getCourses());
            }

            @Override
            public void onFailure(Call<CourseList> call, Throwable t) {
                Log.d(TAG, " FAILED >>> " + t.toString());
            }
        });
    }
}
