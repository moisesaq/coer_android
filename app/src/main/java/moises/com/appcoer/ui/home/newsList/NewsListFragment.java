package moises.com.appcoer.ui.home.newsList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.NewsList;
import moises.com.appcoer.model.User;
import moises.com.appcoer.tools.EndlessRecyclerOnScrollListener;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.adapters.NewsListAdapter;
import moises.com.appcoer.ui.home.news.NewsFragment;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListFragment extends BaseFragment implements NewsListAdapter.CallBack{

    private static final String ARG_PARAM1 = "outstanding";
    private static final String TAG = NewsListFragment.class.getSimpleName();

    private View view;
    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;
    @BindView(R.id.loading_view) protected LoadingView mLoadingView;
    @BindView(R.id.progressBar) protected ProgressBar mProgressBar;

    private NewsListAdapter mNewsListAdapter;
    private boolean outstanding;

    public static NewsListFragment newInstance(boolean outstanding) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_PARAM1, outstanding);
        NewsListFragment newsListFragment =  new NewsListFragment();
        newsListFragment.setArguments(bundle);
        return newsListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            outstanding = getArguments().getBoolean(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            ButterKnife.bind(this, view);
            setupView();
        }
        setTitle(outstanding ? getString(R.string.last_news) : getString(R.string.nav_news), R.id.nav_news);
        return view;
    }

    private void setupView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNewsListAdapter = new NewsListAdapter(getContext(), new ArrayList<News>(), this);
        mRecyclerView.setAdapter(mNewsListAdapter);
        mLoadingView.showLoading(mRecyclerView);
        getNews(1);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getNews(currentPage);
            }
        });
    }

    private void getNews(final int page) {
        if(page > 1)
            mProgressBar.setVisibility(View.VISIBLE);
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        User user = Session.getInstance().getUser();
        Call<NewsList> newsListCall = apiClient.getNews(null, page, 0, user == null ? null : Session.getInstance().getUser().getApiToken());
        newsListCall.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                if(response.isSuccessful() && response.body() != null && response.body().getNews() != null && response.body().getNews().size() > 0){
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                    mLoadingView.hideLoading("", mRecyclerView);
                    mNewsListAdapter.addItems(response.body().getNews());
                }else if(page == 1){
                    mLoadingView.hideLoading(getSafeString(R.string.message_withot_news), mRecyclerView);
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                mLoadingView.hideLoading(getSafeString(R.string.message_something_went_wrong), mRecyclerView);
            }
        });
    }

    @Override
    public void onNewsClick(News news) {
        replaceFragment(NewsFragment.newInstance(news), true);
    }

}
