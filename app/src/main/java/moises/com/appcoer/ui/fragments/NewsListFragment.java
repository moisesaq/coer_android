package moises.com.appcoer.ui.fragments;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.NewsList;
import moises.com.appcoer.model.User;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.fragments.adapters.NewsListAdapter;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListFragment extends BaseFragment implements NewsListAdapter.CallBack{

    private static final String TAG = NewsListFragment.class.getSimpleName();

    private View view;
    private RecyclerView mRecyclerView;
    private LoadingView mLoadingView;
    private NewsListAdapter mNewsListAdapter;

    public NewsListFragment() {
        // Required empty public constructor
    }

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
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
        mNewsListAdapter = new NewsListAdapter(getContext(), new ArrayList<News>(), this);
        mRecyclerView.setAdapter(mNewsListAdapter);
        mLoadingView.showLoading(mRecyclerView);
        getNews();
    }

    private void getNews() {
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        User user = Session.getInstance().getUser();
        Call<NewsList> newsListCall = apiClient.getNews(5, 1, 1, user == null ? null : Session.getInstance().getUser().getApiToken());
        newsListCall.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                mLoadingView.hideLoading("", mRecyclerView);
                mNewsListAdapter.addItems(response.body().getNews());
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                Log.d(TAG, " FAILED >>> " + t.toString());
            }
        });
    }

    @Override
    public void onNewsClick(News news) {
        replaceFragment(NewsFragment.newInstance(news), true);
    }

}
